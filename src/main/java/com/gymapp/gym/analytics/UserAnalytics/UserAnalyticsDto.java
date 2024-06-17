package com.gymapp.gym.analytics.UserAnalytics;

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
  private Double currentWeight;
  private Double initialWeight;
  private Double weightPercentageIncrease;
  private Double currentBodyFatPercentage;
  private Double initialBodyFatPercentage;
  private Double bodyFatPercentageIncrease;
  private Double currentBMI;
  private Double initialBMI;
  private Double BMIPercentageIncrease;
  private int workOutDaysDone;
  private Double currentLongestWorkout;
  private Double initialLongestWorkout;
  private Double longestWorkOutPercentageIncrease;
  private Double currentSlowWaveSleep;
  private Double initialSlowWaveSleep;
  private Double slowWaveSleepIncrease;

}