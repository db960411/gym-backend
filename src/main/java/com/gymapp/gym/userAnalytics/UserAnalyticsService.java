package com.gymapp.gym.userAnalytics;

import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

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

    public List<UserAnalyticsDto> getAllByUser(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User can't be null when fetching user analytics data");
        }

       List<UserAnalytics> userAnalytics = userAnalyticsRepository.findAllByUserId(user.getId());

        return toDto(userAnalytics);
    }

    public void createUserAnalyticsForUser(UserAnalytics analytics) {
        analytics.setCreatedAt(Date.from(Instant.now()));
        analytics.setModifiedAt(Date.from(Instant.now()));

        userAnalyticsRepository.save(analytics);
    }

    public void newUpdatedUserAnalyticsForUser(UserAnalytics analytics) {
        analytics.setCreatedAt(Date.from(Instant.now()));
        analytics.setModifiedAt(Date.from(Instant.now()));

        userAnalyticsRepository.save(analytics);
    }

    public List<UserAnalyticsDto> toDto(List<UserAnalytics> userAnalytics) {
        if (userAnalytics == null) {
            return null;
        }

        List<UserAnalyticsDto> userAnalyticsDtoList = new ArrayList<>();

        userAnalytics.forEach(ua -> {
            UserAnalyticsDto userAnalyticsDto = new UserAnalyticsDto();

            userAnalyticsDto.setExerciseType(ua.getExerciseType());

            userAnalyticsDto.setInitialUserWeight(ua.getInitialUserWeight());
            userAnalyticsDto.setCurrentUserWeight(ua.getCurrentUserWeight());
            userAnalyticsDto.setUserWeightPercentageIncrease(ua.getUserWeightPercentageIncrease());

            userAnalyticsDto.setInitialProgressSets(ua.getInitialProgressSets());
            userAnalyticsDto.setCurrentProgressSets(ua.getCurrentProgressSets());
            userAnalyticsDto.setUserProgressSetsPercentageIncrease(ua.getUserProgressSetsPercentageIncrease());

            userAnalyticsDto.setInitialProgressReps(ua.getInitialProgressReps());
            userAnalyticsDto.setCurrentProgressReps(ua.getCurrentProgressReps());
            userAnalyticsDto.setUserProgressRepsPercentageIncrease(ua.getUserProgressRepsPercentageIncrease());

            userAnalyticsDto.setInitialProgressWeight(ua.getInitialProgressWeight());
            userAnalyticsDto.setCurrentProgressWeight(ua.getCurrentProgressWeight());
            userAnalyticsDto.setUserProgressWeightPercentageIncrease(ua.getUserProgressRepsPercentageIncrease());

            userAnalyticsDtoList.add(userAnalyticsDto);
        });


        return userAnalyticsDtoList;
    }


}
