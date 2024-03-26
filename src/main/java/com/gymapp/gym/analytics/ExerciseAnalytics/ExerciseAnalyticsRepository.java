package com.gymapp.gym.analytics.ExerciseAnalytics;

import com.gymapp.gym.exerciseType.ExerciseType;
import com.gymapp.gym.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseAnalyticsRepository extends JpaRepository<ExerciseAnalytics, UUID> {


    ExerciseAnalytics findByUserAndExerciseType(User user, ExerciseType exerciseType);

    List<ExerciseAnalytics> findAllByUser(User user);

}
