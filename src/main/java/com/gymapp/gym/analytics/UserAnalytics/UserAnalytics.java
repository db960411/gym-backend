package com.gymapp.gym.analytics.UserAnalytics;

import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class UserAnalytics {
    @GeneratedValue
    @Id
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    @Column(name = "currentWeight")
    private Double currentWeight;
    @Column(name = "initialWeight")
    private Double initialWeight;
    @Column(name = "weightPercentageIncrease")
    private Double weightPercentageIncrease;
    @Column(name = "currentBodyFatPercentage")
    private Double currentBodyFatPercentage;
    @Column(name = "initialBodyFatPercentage")
    private Double initialBodyFatPercentage;
    @Column(name = "bodyFatPercentageIncrease")
    private Double bodyFatPercentageIncrease;
    @Column(name = "initial_BMI")
    private Double initialBMI;
    @Column(name = "current_BMI")
    private Double currentBMI;
    @Column(name = "workoutDaysDone")
    private int workOutDaysDone;
    @Column(name = "initial_longestWorkOut")
    private Double initialLongestWorkout;
    @Column(name = "current_LongestWorkOut")
    private Double currentLongestWorkout;
    @Column(name = "initial_slowWaveSleep")
    private Double initialSlowWaveSleep;
    @Column(name = "current_slowWaveSleep")
    private Double currentSlowWaveSleep;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
