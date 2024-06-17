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
    private Double currentReps;
    private Double initialReps;
    private Double repsPercentageIncrease;
    private Double currentSets;
    private Double initialSets;
    private Double setsPercentageIncrease;
    private Double currentWeight;
    private Double initialWeight;
    private Double weightPercentageIncrease;
    private String exerciseTypeName;
    private Double distance;
    private Double time;
}
