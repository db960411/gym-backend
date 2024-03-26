package com.gymapp.gym.analytics.UserAnalytics;

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
}