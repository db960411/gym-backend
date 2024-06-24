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
    private double repsPercentageIncrease;
    private double currentSets;
    private double initialSets;
    private double setsPercentageIncrease;
    private double currentWeight;
    private double initialWeight;
    private double weightPercentageIncrease;
    private String exerciseTypeName;
    private double distance;
    private double time;
    private double steps;
    private double BPM;
}
