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
  private double weightIncrease;
  private double currentBodyFat;
  private double initialBodyFat;
  private double bodyFatIncrease;
  private double currentBMI;
  private double initialBMI;
  @JsonProperty("BMI  Increase")
  private double BMIIncrease;
  private int workOutDaysDone;
  private double currentLongestWorkout;
  private double initialLongestWorkout;
  private double longestWorkOutIncrease;
  private double currentSlowWaveSleep;
  private double initialSlowWaveSleep;
  private double slowWaveSleepIncrease;

}