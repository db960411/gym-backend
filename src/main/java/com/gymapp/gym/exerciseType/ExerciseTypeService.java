package com.gymapp.gym.exerciseType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseTypeService {
    @Autowired
    private ExerciseTypeRepository repository;

    public ExerciseType getOrCreateExerciseType(String exerciseName, String exerciseCategory) {
        ExerciseType exerciseType = repository.getByName(exerciseName);

        if (exerciseType == null) {
            ExerciseType newExerciseType = new ExerciseType();
            newExerciseType.setName(exerciseName);
            newExerciseType.setCategory(exerciseCategory);
            exerciseType = repository.save(newExerciseType);
        }

        return exerciseType;
    }

}
