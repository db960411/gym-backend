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
    private double currentSets;
    private double currentWeight;
    private String exerciseTypeName;
    private double initialReps;
    private double initialSets;
    private double initialWeight;
    private double repsPercentageIncrease;
    private double setsPercentageIncrease;
    private double weightPercentageIncrease;
    private double distance;
    private double time;
}
