package com.gymapp.gym.social.friendshipRequest;

import com.gymapp.gym.notifications.NotificationsCategory;
import com.gymapp.gym.notifications.NotificationsService;
import com.gymapp.gym.profile.Profile;
import com.gymapp.gym.profile.ProfileDto;
import com.gymapp.gym.profile.ProfileService;
import com.gymapp.gym.social.Social;
import com.gymapp.gym.social.SocialService;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserDto;
import com.gymapp.gym.websocket.WSHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendshipRequestService {
    @Autowired
    private FriendshipRequestRepository repository;
    @Autowired
    private SocialService socialService;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private WSHandler websocketHandler;


    public Optional<FriendshipRequest> getFriendShipRequestByReceiverAndSender(Social friendSocial, Social userSocial) {
        return repository.findByReceiverAndSender(friendSocial, userSocial);
    }

    public void createFriendShipRequest(int userSocialId, int friendSocialId ) throws IOException {
        Social userSocial = socialService.getById(userSocialId);
        Social friendSocial = socialService.getById(friendSocialId);

        if (userSocial == null || friendSocial == null) {
            throw new IllegalArgumentException("User doesn't exist");
        }

        if (userSocial == friendSocial) {
            throw new IllegalArgumentException("Can't add yourself as friend");
        }

        Optional<FriendshipRequest> existingFriendshipRequest = getFriendShipRequestByReceiverAndSender(friendSocial, userSocial);

        if (existingFriendshipRequest.isPresent()) {
            return;
        }

        FriendshipRequest friendshipRequest = new FriendshipRequest();
        friendshipRequest.setSender(userSocial);
        friendshipRequest.setReceiver(friendSocial);
        friendshipRequest.setStatus(FriendshipStatus.PENDING);
        repository.save(friendshipRequest);

        sendWSEvent(friendshipRequest, friendSocialId);

        notificationsService.createNotificationForUserSocial(friendSocial, userSocial, "New friend request.", userSocial.getUser().getUsername() + " Added you as a friend", NotificationsCategory.SOCIAL);
    }

    public FriendshipRequest acceptFriendshipRequestByUsers(Integer userSocialId, Integer friendSocialId) {
        Social userSocial = socialService.getById(userSocialId);
        Social friendSocial = socialService.getById(friendSocialId);

        if (friendSocial == null || userSocial == null) {
            throw new IllegalArgumentException("One of the users doesn't exist: userSocialId=" + userSocialId + ", friendSocialId=" + friendSocialId);
        }

        Optional<FriendshipRequest> optionalFriendshipRequest = getFriendShipRequestByReceiverAndSender(userSocial, friendSocial);

        if (optionalFriendshipRequest.isEmpty()) {
           throw new IllegalArgumentException("No friend request was found");
        }

        if (optionalFriendshipRequest.get().getStatus().equals(FriendshipStatus.ACCEPTED)) {
           return optionalFriendshipRequest.get();
        }

        FriendshipRequest friendshipRequest = optionalFriendshipRequest.get();

        friendshipRequest.setStatus(FriendshipStatus.ACCEPTED);

        return repository.save(friendshipRequest);
    }

    public List<FriendshipRequest> getFriendRequestsByUserAndStatus(Integer socialId, FriendshipStatus status) {
        Social user = socialService.getById(socialId);

        if (user == null) {
            throw new IllegalArgumentException("User doesn't exist");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status is null");
        }

        return repository.getByReceiverAndStatus(user, status);
    }

    private void sendWSEvent(FriendshipRequest friendshipRequest, int recipientSocialId) throws IOException {
        UserDto senderDto = new UserDto();
        var sender = friendshipRequest.getSender().getUser();
        senderDto.setSocialId(friendshipRequest.getSender().getId());
        senderDto.setEmail(sender.getEmail());
        senderDto.setRole(sender.getRole());
        senderDto.setLevel(sender.getLevel());
        senderDto.setImage(sender.getImage());
        ProfileDto senderProfileDto = new ProfileDto();
        Profile senderProfile = profileService.getByUserId(sender.getId());
        if (senderProfile != null) {
            senderProfileDto.setDisplayName(senderProfile.getDisplayName());
        }
        senderDto.setProfileDto(senderProfileDto);

        websocketHandler.handleNewFriendRequest(senderDto, recipientSocialId);
    }
}