package com.gymapp.gym.userAnalytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAnalyticsRepository extends JpaRepository<UserAnalytics, UUID> {

    List<UserAnalytics> findAllByUserId(int userId);

}
