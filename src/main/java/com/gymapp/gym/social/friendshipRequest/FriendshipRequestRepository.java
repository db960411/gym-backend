package com.gymapp.gym.social.friendshipRequest;


import com.gymapp.gym.social.Social;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Integer> {

    FriendshipRequest getByReceiver(Social receiver);

    Set<FriendshipRequest> getByReceiverAndStatus(Social receiver, FriendshipStatus status);

    Optional<FriendshipRequest> findByReceiverAndSender(Social receiver, Social sender);
}


