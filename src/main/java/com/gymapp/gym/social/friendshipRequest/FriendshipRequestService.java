package com.gymapp.gym.social.friendshipRequest;

import com.gymapp.gym.notifications.NotificationsCategory;
import com.gymapp.gym.notifications.NotificationsService;
import com.gymapp.gym.social.Social;
import com.gymapp.gym.social.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FriendshipRequestService {
    @Autowired
    private FriendshipRequestRepository repository;
    @Autowired
    private SocialService socialService;
    @Autowired
    private NotificationsService notificationsService;


    public Optional<FriendshipRequest> getFriendShipRequestByReceiverAndSender(Social friendSocial, Social userSocial) {
        return repository.findByReceiverAndSender(friendSocial, userSocial);
    }

    public void createFriendShipRequest(int userSocialId, int friendSocialId ) {
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
        notificationsService.createNotificationForUserSocial(friendSocial, userSocial, "New friend request.", userSocial.getUser().getUsername() + " Added you as a friend", NotificationsCategory.SOCIAL);
    }

    public FriendshipRequest acceptFriendshipRequestByUsers(Integer userSocialId, Integer friendSocialId) {
        Social userSocial = socialService.getById(userSocialId);
        Social friendSocial = socialService.getById(friendSocialId);

        if (friendSocial == null || userSocial == null) {
            throw new IllegalArgumentException("One of the users doesn't exist: userSocialId=" + userSocialId + ", friendSocialId=" + friendSocialId);
        }

        Optional<FriendshipRequest> optionalFriendshipRequest = getFriendShipRequestByReceiverAndSender(friendSocial, userSocial);

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

    public Set<FriendshipRequest> getFriendRequestsByUserAndStatus(Integer socialId, FriendshipStatus status) {
        Social user = socialService.getById(socialId);

        if (user == null) {
            throw new IllegalArgumentException("User doesn't exist");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status is null");
        }

        return repository.getByReceiverAndStatus(user, status);
    }
}
