package com.gymapp.gym.analytics.UserAnalytics;

import com.gymapp.gym.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAnalyticsRepository extends JpaRepository<UserAnalytics, UUID> {

    UserAnalytics findAllByUserId(int userId);

    UserAnalytics findByUserId(Integer userId);

}
