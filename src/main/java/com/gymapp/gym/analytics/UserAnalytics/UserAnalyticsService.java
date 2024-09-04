package com.gymapp.gym.analytics.UserAnalytics;

import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.gymapp.gym.progress.ProgressService.calculateIncrease;

@Service
public class UserAnalyticsService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAnalyticsRepository userAnalyticsRepository;

    public List<UserAnalyticsDto> getAllByUser(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User can't be null when fetching user analytics data");
        }

       UserAnalytics userAnalytics = userAnalyticsRepository.findByUserId(user.getId());

        return toDto(userAnalytics);

    }

    public void createUserAnalyticsForUser(UserAnalytics analytics) {
        analytics.setCreatedAt(Date.from(Instant.now()));
        analytics.setModifiedAt(Date.from(Instant.now()));

        userAnalyticsRepository.save(analytics);
    }

    public List<UserAnalyticsDto> updateUserAnalyticsForUser(HttpServletRequest request, UserAnalyticsDto userAnalyticsDto) {
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
        if (userAnalyticsDto.getInitialBodyFat() != 0) {
            userAnalytics.setInitialBodyFat(userAnalyticsDto.getInitialBodyFat());
        }
        if (userAnalyticsDto.getCurrentBodyFat() != 0) {
            userAnalytics.setCurrentBodyFat(userAnalyticsDto.getCurrentBodyFat());
        }
        if (userAnalyticsDto.getCurrentWeight() != 0) {
            userAnalytics.setCurrentWeight(userAnalyticsDto.getCurrentWeight());
        }
        if (userAnalyticsDto.getInitialWeight() != 0) {
            userAnalytics.setInitialWeight(userAnalyticsDto.getInitialWeight());
        }

        userAnalyticsRepository.save(userAnalytics);
    }

    public List<UserAnalyticsDto> toDto(UserAnalytics ua) {
        if (ua == null) {
            return Collections.emptyList();
        }

        UserAnalyticsDto userAnalyticsDto = new UserAnalyticsDto();
        userAnalyticsDto.setCurrentWeight(ua.getCurrentWeight());
        userAnalyticsDto.setInitialWeight(ua.getInitialWeight());
        userAnalyticsDto.setWeightIncrease(calculateIncrease(ua.getInitialWeight(), ua.getCurrentWeight()));
        userAnalyticsDto.setInitialBodyFat(ua.getInitialBodyFat());
        userAnalyticsDto.setCurrentBodyFat(ua.getCurrentBodyFat());
        userAnalyticsDto.setBodyFatIncrease(ua.getBodyFatIncrease());
        userAnalyticsDto.setInitialBMI(ua.getInitialBMI());
        userAnalyticsDto.setCurrentBMI(ua.getCurrentBMI());
        userAnalyticsDto.setBMIIncrease(calculateIncrease(ua.getInitialBMI(), ua.getCurrentBMI()));
        userAnalyticsDto.setWorkOutDaysDone(ua.getWorkOutDaysDone());
        userAnalyticsDto.setInitialLongestWorkout(ua.getInitialLongestWorkout());
        userAnalyticsDto.setCurrentLongestWorkout(ua.getCurrentLongestWorkout());
        userAnalyticsDto.setLongestWorkOutIncrease(calculateIncrease(ua.getInitialLongestWorkout(), ua.getCurrentLongestWorkout()));
        userAnalyticsDto.setInitialSlowWaveSleep(ua.getInitialSlowWaveSleep());
        userAnalyticsDto.setCurrentSlowWaveSleep(ua.getCurrentSlowWaveSleep());
        userAnalyticsDto.setSlowWaveSleepIncrease(calculateIncrease(ua.getInitialSlowWaveSleep(), ua.getCurrentSlowWaveSleep()));
        return List.of(userAnalyticsDto);
    }
}
