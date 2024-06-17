package com.gymapp.gym.progress;

import com.gymapp.gym.analytics.ExerciseAnalytics.ExerciseAnalytics;
import com.gymapp.gym.analytics.ExerciseAnalytics.ExerciseAnalyticsService;
import com.gymapp.gym.exerciseType.ExerciseType;
import com.gymapp.gym.exerciseType.ExerciseTypeService;
import com.gymapp.gym.notifications.NotificationsCategory;
import com.gymapp.gym.notifications.NotificationsService;
import com.gymapp.gym.profile.Profile;
import com.gymapp.gym.profile.ProfileService;
import com.gymapp.gym.subscription.Subscription;
import com.gymapp.gym.subscription.SubscriptionService;
import com.gymapp.gym.subscription.SubscriptionType;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import com.gymapp.gym.analytics.UserAnalytics.UserAnalytics;
import com.gymapp.gym.analytics.UserAnalytics.UserAnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.*;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ProgressService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProgressRepository repository;
    @Autowired
    private ExerciseTypeService exerciseTypeService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private ExerciseAnalyticsService exerciseAnalyticsService;

    public List<ProgressDto> getByProfile(HttpServletRequest request) throws IllegalAccessException {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalAccessException("User doesn't exist");
        }

        Profile profile = profileService.getByUserId(user.getId());

        if (profile == null) {
            return Collections.emptyList();
        }

        List<Progress> progressList = repository.findByProfileId(profile.getId());

        return getProgressDtoList(progressList);
    }

    @NotNull
    private static List<ProgressDto> getProgressDtoList(List<Progress> progressList) {
        List<ProgressDto> progressDtoList = new ArrayList<>();
        for (Progress progress: progressList) {
            ProgressDto progressDto = new ProgressDto();
            progressDto.setExerciseType(progress.getExerciseType());
            progressDto.setSets(progress.getSets());
            progressDto.setReps(progress.getReps());
            progressDto.setWeight(progress.getWeight());
            progressDto.setDistance(progress.getDistance());
            progressDto.setTime(progress.getTime());
            progressDto.setId(progress.getId());
            progressDto.setSteps(progress.getSteps());
            progressDto.setHeartRate(progress.getHeartRate());
            progressDtoList.add(progressDto);
        }
        return progressDtoList;
    }

    public ResponseEntity<ProgressDto> addProgressToProfile(HttpServletRequest request, @RequestBody ProgressFormData formData) throws IllegalAccessException {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalAccessException("User doesn't exist");
        }

        Profile profile = profileService.getByUserId(user.getId());

        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        if (formData == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Progress> allByProfile =  repository.getAllByProfileId(profile.getId());

        for (Progress pr : allByProfile) {
            if (pr.getExerciseType().getName().equals(formData.getExerciseType())) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        }
        Subscription userSubscription = subscriptionService.getByUserId(user.getId());

        if (userSubscription.getSubscriptionType().equals(SubscriptionType.BASIC) && allByProfile.size() >= 9) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Progress progress = mapToProgress(formData);
        progress.setProfile(profile);
        repository.save(progress);

        ProgressDto progressDto = getProgressDto(progress);

        notificationsService.addNotificationsToFriendlySendOutQueue(user, user + "added" + progress.getExerciseType().getName() + "progress", NotificationsCategory.PROGRESSION, Date.from(Instant.now()));

        ExerciseAnalytics exerciseAnalytics = getExerciseAnalytics(progress, user);

        exerciseAnalyticsService.createExerciseAnalyticsForUser(exerciseAnalytics);

        return ResponseEntity.ok().body(progressDto);
    }

    @NotNull
    private static ProgressDto getProgressDto(Progress progress) {
        ProgressDto progressDto = new ProgressDto();
        progressDto.setId(progress.getId());
        progressDto.setDistance(progress.getDistance());
        progressDto.setWeight(progress.getWeight());
        progressDto.setSets(progress.getSets());
        progressDto.setReps(progress.getReps());
        progressDto.setExerciseType(progress.getExerciseType());
        progressDto.setDistance(progress.getDistance());
        progressDto.setTime(progress.getTime());
        progressDto.setSteps(progress.getSteps());
        progressDto.setHeartRate(progress.getHeartRate());
        return progressDto;
    }

    @NotNull
    private static ExerciseAnalytics getExerciseAnalytics(Progress progress, User user) {
        ExerciseAnalytics exerciseAnalytics = new ExerciseAnalytics();
        exerciseAnalytics.setUser(user);
        exerciseAnalytics.setExerciseTypeName(progress.getExerciseType().getName());
        exerciseAnalytics.setExerciseType(progress.getExerciseType());
        exerciseAnalytics.setInitialReps(progress.getReps());
        exerciseAnalytics.setInitialSets(progress.getSets());
        exerciseAnalytics.setInitialWeight(progress.getWeight());
        exerciseAnalytics.setDistance(progress.getDistance());
        exerciseAnalytics.setTime(progress.getTime());
        return exerciseAnalytics;
    }

    public ResponseEntity<String> deleteProgressById(HttpServletRequest request, @PathVariable UUID exerciseId) throws IllegalAccessException {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalAccessException("User doesn't exist");
        }

        Progress progress = repository.getById(exerciseId);

        if (progress == null) {
            return ResponseEntity.badRequest().build();
        }

        repository.delete(progress);

        ExerciseAnalytics exerciseAnalytics = exerciseAnalyticsService.findByUserAndExerciseType(user, progress.getExerciseType());
        exerciseAnalyticsService.delete(exerciseAnalytics);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<ProgressDto> editProgressById(HttpServletRequest request, UUID exerciseId, ProgressDto data) throws IllegalAccessException {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalAccessException("User doesn't exist");
        }

        Progress progress = repository.getById(exerciseId);

        if (progress == null) {
            return ResponseEntity.badRequest().build();
        }

        if (data == null) {
            return ResponseEntity.badRequest().build();
        }

        progress.setSets(data.getSets());
        progress.setReps(data.getReps());
        progress.setWeight(data.getWeight());
        progress.setDistance(data.getDistance());
        progress.setTime(data.getTime());

        repository.save(progress);

        ProgressDto progressDto = new ProgressDto();
        progressDto.setId(progress.getId());
        progressDto.setExerciseType(progress.getExerciseType());
        progressDto.setSets(progress.getSets());
        progressDto.setReps(progress.getReps());
        progressDto.setWeight(progress.getWeight());
        progressDto.setDistance(progress.getDistance());
        progressDto.setTime(progress.getTime());
        progressDto.setSteps(progress.getSteps());
        progressDto.setHeartRate(progress.getHeartRate());


        // TODO add distance etc
        ExerciseAnalytics exerciseAnalytics = exerciseAnalyticsService.findByUserAndExerciseType(user, progress.getExerciseType());
        exerciseAnalytics.setCurrentReps(progress.getReps());
        exerciseAnalytics.setRepsPercentageIncrease(calculatePercentageIncrease(exerciseAnalytics.getInitialReps(), data.getReps()));
        exerciseAnalytics.setCurrentSets(progress.getSets());
        exerciseAnalytics.setSetsPercentageIncrease(calculatePercentageIncrease(exerciseAnalytics.getInitialSets(), data.getSets()));
        exerciseAnalytics.setCurrentWeight(progress.getWeight());
        exerciseAnalytics.setWeightPercentageIncrease(calculatePercentageIncrease(exerciseAnalytics.getInitialWeight(), data.getWeight()));

        exerciseAnalyticsService.updateExerciseAnalytics(exerciseAnalytics);

        return ResponseEntity.ok(progressDto);
    }

    public static Double calculatePercentageIncrease(Double initialValue, Double currentValue) {
        if (initialValue == 0 || currentValue == 0) {
            return 0.0;
        }
        // Calculate the difference between current value and initial value
        double difference = currentValue - initialValue;

        // Calculate the percentage increase
        return Math.floor((difference / initialValue) * 100);
    }

    public List<Progress> getAllProgressByProfileId(int id) {
      return repository.getAllByProfileId(id);
    }

    private Progress mapToProgress(ProgressFormData formData) {
        Progress progress = new Progress();
        ExerciseType exerciseType = exerciseTypeService.getOrCreateExerciseType(formData.getExerciseType());
        progress.setExerciseType(exerciseType);
        progress.setSets((int) formData.getSets());
        progress.setReps((int) formData.getReps());
        progress.setWeight(formData.getWeight());
        progress.setDistance(formData.getDistance());
        progress.setTime(formData.getTime());
        progress.setSteps(formData.getSteps());
        progress.setHeartRate(formData.getHeartRate());
        return progress;
    }


}
