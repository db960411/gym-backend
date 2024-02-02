package com.gymapp.gym.social;

import com.gymapp.gym.notifications.Notifications;
import com.gymapp.gym.notifications.NotificationsCategory;
import com.gymapp.gym.notifications.NotificationsService;
import com.gymapp.gym.plans.plan_progression.PlanProgression;
import com.gymapp.gym.plans.plan_progression.PlanProgressionDto;
import com.gymapp.gym.plans.plan_progression.PlanProgressionService;
import com.gymapp.gym.profile.Profile;
import com.gymapp.gym.profile.ProfileDto;
import com.gymapp.gym.profile.ProfileService;
import com.gymapp.gym.progress.Progress;
import com.gymapp.gym.progress.ProgressDto;
import com.gymapp.gym.progress.ProgressService;
import com.gymapp.gym.social.friendshipRequest.FriendShipRequestDto;
import com.gymapp.gym.social.friendshipRequest.FriendshipRequest;
import com.gymapp.gym.social.friendshipRequest.FriendshipRequestService;
import com.gymapp.gym.social.friendshipRequest.FriendshipStatus;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserDto;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialService {
    @Autowired
    private SocialRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private SocialService socialService;
    @Autowired
    private FriendshipRequestService friendshipRequestService;
    @Autowired
    private PlanProgressionService planProgressionService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProgressService progressService;
    @Autowired
    private NotificationsService notificationsService;


    public Social getById(int socialId) {
        return repository.getById(socialId);
    }

    public Social getByUserId(int userId) {
        return repository.getByUserId(userId);
    }

    public void createSocialForUser(User user) {
        user = userService.getUserById(user.getId());

        if (user == null) {
            throw new IllegalArgumentException("User not found when trying to add social entity");
        }

        Social social = repository.getByUserId(user.getId());

        if (social != null) {
            throw new RuntimeException("User already has social");
        }

        social = Social.builder().user(user).id(generateRandomSocialId()).build();

        repository.save(social);
    }

    public SocialDto getOrCreateSocialForUser(@NotNull HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        Social socialByUser = repository.getByUserId(user.getId());

        if (socialByUser != null) {
            return toDto(socialByUser);
        }

        int randomSocialId = generateRandomSocialId();
        Social newSocial = Social.builder().user(user).id(randomSocialId).build();

        repository.save(newSocial);

        return toDto(newSocial);
    }

    public SocialDto addFriendForUser(@NotNull HttpServletRequest request, @RequestBody int friendSocialId) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Social userSocial = socialService.getByUserId(user.getId());

        if (userSocial == null) {
            throw new IllegalArgumentException("User without social tried to add a friend");
        }

        Optional<Social> optionalFriendSocial = repository.findById(friendSocialId);

        if (optionalFriendSocial.isEmpty()) {
            return SocialDto.builder().errorMessage("Found no social id by: " + friendSocialId).build();
        }

        Social friendSocial = optionalFriendSocial.get();

        if (userSocial.getFriends().contains(friendSocial)) {
            return SocialDto.builder().errorMessage("You are already friends with this user").build();
        }

        if (userSocial == friendSocial) {
            return SocialDto.builder().errorMessage("You can't add yourself as a friend").build();
        }

        Optional<FriendshipRequest> existingFriendShipRequest = friendshipRequestService.getFriendShipRequestByReceiverAndSender(friendSocial, userSocial);

        if (existingFriendShipRequest.isPresent()) {
            return SocialDto.builder().errorMessage("You have already sent a friendrequest to this user").build();
        }

        User friend = userService.getUserByEmail(friendSocial.getUser().getEmail());

        if (friend != null) {
            friendshipRequestService.createFriendShipRequest(userSocial.getId(), friendSocialId);
        }

        return toDto(userSocial);
    }

    public SocialDto acceptFriendshipRequestForUser(@NotNull HttpServletRequest request, @RequestBody int friendSocialId) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Social userSocial = repository.getByUserId(user.getId());

        if (userSocial == null) {
            throw new IllegalArgumentException("User without social tried to accept a friend");
        }

        Social friendSocial = repository.getById(friendSocialId);

        if (friendSocial.getId() == null) {
            return SocialDto.builder().errorMessage("Friend doesn't exist by social id: " + friendSocialId).build();
        }

        Optional<FriendshipRequest> existingFriendShipRequest = friendshipRequestService.getFriendShipRequestByReceiverAndSender(userSocial, friendSocial);

        if (existingFriendShipRequest.isEmpty()) {
            return SocialDto.builder().errorMessage("No friend request is pending with friend: " + friendSocial.getId()).build();
        }

        if (existingFriendShipRequest.get().getStatus().equals(FriendshipStatus.ACCEPTED)) {
            return SocialDto.builder().errorMessage("You are already friends with this user with social id: " + friendSocial.getId()).build();
        }

       FriendshipRequest friendshipRequest = friendshipRequestService.acceptFriendshipRequestByUsers(userSocial.getId(), friendSocial.getId());

        if (friendshipRequest == null) {
            return SocialDto.builder().errorMessage("There is no friend request pending").build();
        }

        userSocial.getFriends().add(friendSocial);
        friendSocial.getFriends().add(userSocial);

        repository.save(userSocial);
        repository.save(friendSocial);
        notificationsService.createNotificationForUserSocial(friendSocial, userSocial," Accepted your friend request.", null, NotificationsCategory.SOCIAL);

        return toDto(userSocial);
    }


    public List<ProgressDto> getProgressOfFriend(HttpServletRequest request, int friendSocialId) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User doesn't exist");
        }

        Social social = socialService.getById(friendSocialId);

        if (social == null) {
            throw new IllegalArgumentException("Social doesn't exist");
        }

        Profile profile = profileService.getByUserId(social.getUser().getId());

        if (profile == null) {
            return Collections.emptyList();
        }

        List<Progress> progress = progressService.getAllProgressByProfileId(profile.getId());

        if (progress == null) {
            throw new IllegalArgumentException("Progress doesn't exist");
        }

        List<ProgressDto> progressDtoList = new ArrayList<>();
        progress.forEach(pr -> {
            ProgressDto progressDto = new ProgressDto();
            progressDto.setSets(pr.getSets());
            progressDto.setReps(pr.getReps());
            progressDto.setWeight(pr.getWeight());
            progressDto.setExerciseType(pr.getExerciseType());
            progressDto.setDistance(pr.getDistance());
            progressDto.setTime(pr.getTime());
            progressDtoList.add(progressDto);
        });

        return progressDtoList;
    }


    public SocialDto toDto(Social social) {
        if (social == null) {
            return null;
        }

        SocialDto socialDto = new SocialDto();
        socialDto.setUserSocialId(social.getId());

        User user = social.getUser();
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setLevel(user.getLevel());
        userDto.setProfileImageUrl(user.getProfileImageUrl());

        ProfileDto profileDto = new ProfileDto();
        Profile userProfile = profileService.getByUserId(user.getId());

        if (userProfile != null) {
            profileDto.setGender(userProfile.getGender());
            profileDto.setLanguage(userProfile.getLanguage());
            profileDto.setNationality(userProfile.getNationality());
            profileDto.setDateOfBirth(userProfile.getDateOfBirth());
            profileDto.setDisplayName(userProfile.getDisplayName());
            socialDto.setProfileInfo(profileDto);
        }

        Set<FriendshipRequest> allFriendRequests = friendshipRequestService.getFriendRequestsByUserAndStatus(social.getId(), FriendshipStatus.PENDING);

        if (!allFriendRequests.isEmpty()) {
            FriendShipRequestDto friendShipRequestDto = new FriendShipRequestDto();
            Set<UserDto> senderDtos = allFriendRequests.stream()
                    .map(FriendshipRequest::getSender)
                    .map(sender -> {
                        UserDto senderDto = new UserDto();
                        senderDto.setSocialId(sender.getId());
                        senderDto.setEmail(sender.getUser().getEmail());
                        senderDto.setRole(sender.getUser().getRole());
                        senderDto.setLevel(sender.getUser().getLevel());
                        senderDto.setProfileImageUrl(sender.getUser().getProfileImageUrl());
                        ProfileDto senderProfileDto = new ProfileDto();
                        Profile senderProfile = profileService.getByUserId(sender.getId());
                        if (senderProfile != null) {
                            senderProfileDto.setDisplayName(senderProfile.getDisplayName());
                        }
                        senderDto.setProfileDto(senderProfileDto);
                        return senderDto;
                    })
                    .collect(Collectors.toSet());

            friendShipRequestDto.setUserInfo(senderDtos);
            socialDto.setFriendRequests(friendShipRequestDto);
        }

        PlanProgression userPlanProgression = planProgressionService.getPlanProgressionByUserId(user.getId());

        if (userPlanProgression != null) {
            PlanProgressionDto planProgressionDto = new PlanProgressionDto();
            planProgressionDto.setDay(userPlanProgression.getDay());
            planProgressionDto.setPlan(userPlanProgression.getPlan());
            socialDto.setPlanProgressionDto(planProgressionDto);
        }

        socialDto.setUserInfo(userDto);

        Set<SocialFriendsDto> socialFriends = new HashSet<>();
        if (social.getFriends() != null) {
            for (Social friend : social.getFriends()) {

                User friendUser = friend.getUser();
                UserDto friendDto = new UserDto();
                friendDto.setLevel(friendUser.getLevel());
                friendDto.setEmail(friendUser.getEmail());
                friendDto.setRole(friendUser.getRole());
                friendDto.setProfileImageUrl(friendUser.getProfileImageUrl());
                SocialFriendsDto socialFriendsDto = new SocialFriendsDto();
                socialFriendsDto.setUserSocialId(friend.getId());
                socialFriendsDto.setUserInfo(friendDto);
                socialFriends.add(socialFriendsDto);

                ProfileDto socialProfileDto = new ProfileDto();
                Profile friendProfile = profileService.getByUserId(friendUser.getId());

                if (friendProfile != null) {
                    socialProfileDto.setGender(friendProfile.getGender());
                    socialProfileDto.setLanguage(friendProfile.getLanguage());
                    socialProfileDto.setNationality(friendProfile.getNationality());
                    socialProfileDto.setDateOfBirth(friendProfile.getDateOfBirth());
                    socialProfileDto.setDisplayName(friendProfile.getDisplayName());
                    socialFriendsDto.setProfileDto(socialProfileDto);
                }

                PlanProgression friendPlanProgression = planProgressionService.getPlanProgressionByUserId(friendUser.getId());

                if (friendPlanProgression != null) {
                    PlanProgressionDto friendPlanProgressionDto = new PlanProgressionDto();
                    friendPlanProgressionDto.setDay(friendPlanProgression.getDay());
                    friendPlanProgressionDto.setPlan(friendPlanProgression.getPlan());

                    socialFriendsDto.setPlanProgressionDto(friendPlanProgressionDto);
                }

            }
        }

        socialDto.setFriends(socialFriends);

        return socialDto;
    }

    private int generateRandomSocialId() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
}
