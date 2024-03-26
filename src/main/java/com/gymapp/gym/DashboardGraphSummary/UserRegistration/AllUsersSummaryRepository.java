package com.gymapp.gym.DashboardGraphSummary.UserRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AllUsersSummaryRepository extends JpaRepository<AllUsersSummary, UUID> {

    List<AllUsersSummary> findAll();

    AllUsersSummary findFirstByOrderByCreatedAtDesc();
}
