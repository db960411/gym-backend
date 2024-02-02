package com.gymapp.gym.subscription;


import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Subscription findByUserId(Integer userid);

    Boolean existsByUserEmail(String email);
}
