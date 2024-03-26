package com.gymapp.gym.progress;

import com.gymapp.gym.exerciseType.ExerciseType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressFormData {
    private String exerciseType;
    private double sets;
    private double reps;
    private double weight;
    private double distance;
    private double time;
}
