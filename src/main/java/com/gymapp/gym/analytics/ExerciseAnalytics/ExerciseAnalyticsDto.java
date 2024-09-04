package com.gymapp.gym.analytics.ExerciseAnalytics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExerciseAnalyticsDto {
    private double currentReps;
    private double initialReps;
    private double repsIncrease;
    private double currentSets;
    private double initialSets;
    private double setsIncrease;
    private double currentWeight;
    private double initialWeight;
    private double weightIncrease;
    private String exerciseTypeName;
    private String exerciseTypeCategory;
    private double distance;
    private double time;
    private double steps;
    private double BPM;
}
