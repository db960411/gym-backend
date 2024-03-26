package com.gymapp.gym.subscription;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Subscription findByUserId(Integer userid);

    Boolean existsByUserEmail(String email);

}
