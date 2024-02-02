package com.gymapp.gym.userAnalytics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserAnalyticsDto {
    private String exerciseType;
    private double currentProgressReps;
    private double currentProgressSets;
    private double currentProgressWeight;
    private double currentUserWeight;
    private double initialProgressReps;
    private double initialProgressSets;
    private double initialProgressWeight;
    private double initialUserWeight;
    private double userProgressRepsPercentageIncrease;
    private double userProgressSetsPercentageIncrease;
    private double userProgressWeightPercentageIncrease;
    private double userProgress;
    private double userWeightPercentageIncrease;
    private Date createdAt;


}
