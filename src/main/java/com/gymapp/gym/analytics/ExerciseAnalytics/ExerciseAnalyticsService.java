package com.gymapp.gym.analytics.ExerciseAnalytics;

import com.gymapp.gym.exerciseType.ExerciseType;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.gymapp.gym.progress.ProgressService.calculateIncrease;

@Service
public class ExerciseAnalyticsService {
    @Autowired
    private ExerciseAnalyticsRepository exerciseAnalyticsRepository;
    @Autowired
    private UserService userService;

    public void createExerciseAnalyticsForUser(ExerciseAnalytics exerciseAnalytics) {
        exerciseAnalytics.setCreatedAt(Date.from(Instant.now()));
        exerciseAnalytics.setModifiedAt(Date.from(Instant.now()));

        exerciseAnalyticsRepository.save(exerciseAnalytics);
    }

    public void updateExerciseAnalytics(ExerciseAnalytics exerciseAnalytics) {
        exerciseAnalytics.setModifiedAt(java.util.Date.from(Instant.now()));

        exerciseAnalyticsRepository.save(exerciseAnalytics);
    }

    public ExerciseAnalytics findByUserAndExerciseType(User user, ExerciseType exerciseType) {
        return exerciseAnalyticsRepository.findByUserAndExerciseType(user, exerciseType);
    }

    public void delete(ExerciseAnalytics exerciseAnalytics) {
        if (exerciseAnalytics != null) {
            exerciseAnalyticsRepository.delete(exerciseAnalytics);
        }
    }

    public List<ExerciseAnalyticsDto> getAllByUser(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User null when trying to get all exercise analytics by user");
        }

        List<ExerciseAnalytics> exerciseAnalyticsList = exerciseAnalyticsRepository.findAllByUser(user);

        return getExerciseAnalyticsDtoList(exerciseAnalyticsList);
    }

    @NotNull
    private List<ExerciseAnalyticsDto> getExerciseAnalyticsDtoList(List<ExerciseAnalytics> exerciseAnalyticsList) {
        List<ExerciseAnalyticsDto> exerciseAnalyticsDtos = new ArrayList<>();
        for (ExerciseAnalytics exerciseAnalytics: exerciseAnalyticsList) {
            ExerciseAnalyticsDto exerciseAnalyticsDto = new ExerciseAnalyticsDto();
            exerciseAnalyticsDto.setExerciseTypeName(exerciseAnalytics.getExerciseType().getName());
            exerciseAnalyticsDto.setExerciseTypeCategory(exerciseAnalytics.getExerciseType().getCategory());
            exerciseAnalyticsDto.setInitialSets(exerciseAnalytics.getInitialSets());
            exerciseAnalyticsDto.setCurrentSets(exerciseAnalytics.getCurrentSets());
            exerciseAnalyticsDto.setSetsIncrease(exerciseAnalytics.getSetsIncrease());
            exerciseAnalyticsDto.setInitialReps(exerciseAnalytics.getInitialReps());
            exerciseAnalyticsDto.setCurrentReps(exerciseAnalytics.getCurrentReps());
            exerciseAnalyticsDto.setRepsIncrease(exerciseAnalytics.getRepsIncrease());
            exerciseAnalyticsDto.setInitialWeight(exerciseAnalytics.getInitialWeight());
            exerciseAnalyticsDto.setCurrentWeight(exerciseAnalytics.getCurrentWeight());
            exerciseAnalyticsDto.setWeightIncrease(calculateIncrease(exerciseAnalytics.getInitialWeight(), exerciseAnalytics.getCurrentWeight()));
            exerciseAnalyticsDto.setDistance(exerciseAnalytics.getDistance());
            exerciseAnalyticsDto.setTime(exerciseAnalytics.getTime());
            exerciseAnalyticsDto.setBPM(exerciseAnalytics.getBPM());
            exerciseAnalyticsDto.setSteps(exerciseAnalytics.getSteps());
            exerciseAnalyticsDtos.add(exerciseAnalyticsDto);
        }

        return exerciseAnalyticsDtos;
    }
}
