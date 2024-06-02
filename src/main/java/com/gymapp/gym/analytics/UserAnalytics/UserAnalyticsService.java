package com.gymapp.gym.analytics.UserAnalytics;

import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserAnalyticsService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAnalyticsRepository userAnalyticsRepository;

    public UserAnalyticsDto getAllByUser(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User can't be null when fetching user analytics data");
        }

       UserAnalytics userAnalytics = userAnalyticsRepository.findAllByUserId(user.getId());

        return toDto(userAnalytics);
    }

    public void createUserAnalyticsForUser(UserAnalytics analytics) {
        analytics.setCreatedAt(Date.from(Instant.now()));
        analytics.setModifiedAt(Date.from(Instant.now()));

        userAnalyticsRepository.save(analytics);
    }

    public UserAnalyticsDto updateUserAnalyticsForUser(HttpServletRequest request, UserAnalyticsDto userAnalyticsDto) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (userAnalyticsDto == null) {
            return null;
        }

        if (user == null) {
            throw new RuntimeException("User can't be null");
        }

        UserAnalytics userAnalytics = userAnalyticsRepository.findByUserId(user.getId());

        if (userAnalytics == null) {
            throw new RuntimeException("No user analytics found by user.");
        }

        setNewAnalyticsData(userAnalyticsDto, userAnalytics);

       return toDto(userAnalytics);
    }

    public UserAnalytics getByUser(User user) {
        return userAnalyticsRepository.findByUserId(user.getId());
    }

    public void updatedUserAnalyticsForUser(UserAnalytics analytics) {
        analytics.setModifiedAt(Date.from(Instant.now()));

        userAnalyticsRepository.save(analytics);
    }

    public double calculateBMI(double weight, double height) {
        // Convert height to meters
        var heightInMeters = height / 100; // assuming height is in centimeters

        // Calculate BMI
        return weight / (heightInMeters * heightInMeters);
    }

    public void setNewAnalyticsData(UserAnalyticsDto userAnalyticsDto, UserAnalytics userAnalytics) {
        if (userAnalyticsDto.getInitialLongestWorkout() != 0) {
            userAnalytics.setInitialLongestWorkout(userAnalyticsDto.getInitialLongestWorkout());
        }
        if (userAnalyticsDto.getCurrentLongestWorkout() != 0) {
            userAnalytics.setCurrentLongestWorkout(userAnalyticsDto.getCurrentLongestWorkout());
        }
        if (userAnalyticsDto.getWorkOutDaysDone() != 0) {
            userAnalytics.setWorkOutDaysDone(userAnalyticsDto.getWorkOutDaysDone());
        }
        if (userAnalyticsDto.getInitialSlowWaveSleep() != 0) {
            userAnalytics.setInitialSlowWaveSleep(userAnalyticsDto.getInitialSlowWaveSleep());
        }
        if (userAnalyticsDto.getInitialBodyFatPercentage() != 0) {
            userAnalytics.setInitialBodyFatPercentage(userAnalyticsDto.getInitialBodyFatPercentage());
        }
        if (userAnalyticsDto.getCurrentBodyFatPercentage() != 0) {
            userAnalytics.setCurrentBodyFatPercentage(userAnalyticsDto.getCurrentBodyFatPercentage());
        }
        if (userAnalyticsDto.getCurrentWeight() != 0) {
            userAnalytics.setCurrentWeight(userAnalyticsDto.getCurrentWeight());
        }
        if (userAnalyticsDto.getInitialWeight() != 0) {
            userAnalytics.setInitialWeight(userAnalyticsDto.getInitialWeight());
        }

        userAnalyticsRepository.save(userAnalytics);
    }

    public UserAnalyticsDto toDto(UserAnalytics ua) {
        if (ua == null) {
            return null;
        }

        UserAnalyticsDto userAnalyticsDto = new UserAnalyticsDto();
        userAnalyticsDto.setCurrentWeight(ua.getCurrentWeight());
        userAnalyticsDto.setInitialWeight(ua.getInitialWeight());
        userAnalyticsDto.setWeightPercentageIncrease(ua.getWeightPercentageIncrease());
        userAnalyticsDto.setInitialBodyFatPercentage(ua.getInitialBodyFatPercentage());
        userAnalyticsDto.setCurrentBodyFatPercentage(ua.getCurrentBodyFatPercentage());
        userAnalyticsDto.setBodyFatPercentageIncrease(ua.getBodyFatPercentageIncrease());
        userAnalyticsDto.setInitialBMI(ua.getInitialBMI());
        userAnalyticsDto.setCurrentBMI(ua.getCurrentBMI());
        userAnalyticsDto.setWorkOutDaysDone(ua.getWorkOutDaysDone());
        userAnalyticsDto.setInitialLongestWorkout(ua.getInitialLongestWorkout());
        userAnalyticsDto.setCurrentLongestWorkout(ua.getCurrentLongestWorkout());
        userAnalyticsDto.setInitialSlowWaveSleep(ua.getInitialSlowWaveSleep());
        userAnalyticsDto.setCurrentSlowWaveSleep(ua.getCurrentSlowWaveSleep());
        return userAnalyticsDto;
    }




}
