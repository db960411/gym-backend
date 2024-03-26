package com.gymapp.gym.DashboardGraphSummary.UserRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRegistrationSummaryRepository extends JpaRepository<UserRegistrationSummary, UUID> {

    List<UserRegistrationSummary> findAll();

    UserRegistrationSummary findFirstByOrderByCreatedAtDesc();
}
