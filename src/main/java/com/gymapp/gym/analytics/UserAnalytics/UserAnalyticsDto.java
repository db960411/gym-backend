package com.gymapp.gym.analytics.UserAnalytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserAnalyticsDto {
  private double currentWeight;
  private double initialWeight;
  private double weightPercentageIncrease;
  private double currentBodyFatPercentage;
  private double initialBodyFatPercentage;
  private double bodyFatPercentageIncrease;
  private double currentBMI;
  private double initialBMI;
  @JsonProperty("BMI Percentage Increase")
  private double BMIPercentageIncrease;
  private int workOutDaysDone;
  private double currentLongestWorkout;
  private double initialLongestWorkout;
  private double longestWorkOutPercentageIncrease;
  private double currentSlowWaveSleep;
  private double initialSlowWaveSleep;
  private double slowWaveSleepIncrease;

}