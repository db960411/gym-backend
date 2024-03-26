package com.gymapp.gym.subcriptionEvents;

import com.gymapp.gym.subscription.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionEventRepository extends JpaRepository<SubscriptionEvent, UUID> {

    List<SubscriptionEvent> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

}
