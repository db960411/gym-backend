package com.gymapp.gym.exerciseType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExerciseTypeRepository extends JpaRepository<ExerciseType, UUID> {

    ExerciseType getByName(String name);
}
