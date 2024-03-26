package com.gymapp.gym.DashboardGraphSummary.SubscriptionSummary;

import com.gymapp.gym.DashboardGraphSummary.UserRegistration.UserRegistrationSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionSummaryRepository extends JpaRepository<SubscriptionSummary, UUID> {

    List<SubscriptionSummary> findAll();

    SubscriptionSummary findFirstByOrderByCreatedAtDesc();
}
