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
    private double currentWeight;
    @Column(name = "initialWeight")
    private double initialWeight;
    @Column(name = "weightIncrease")
    private double weightIncrease;
    @Column(name = "currentBodyFat")
    private double currentBodyFat;
    @Column(name = "initialBodyFat")
    private double initialBodyFat;
    @Column(name = "bodyFatIncrease")
    private double bodyFatIncrease;
    @Column(name = "initial_BMI")
    private double initialBMI;
    @Column(name = "current_BMI")
    private double currentBMI;
    @Column(name = "BMI_Increase")
    private double BMIIncrease;
    @Column(name = "workoutDaysDone")
    private int workOutDaysDone;
    @Column(name = "initial_longestWorkOut")
    private double initialLongestWorkout;
    @Column(name = "current_LongestWorkOut")
    private double currentLongestWorkout;
    @Column(name = "initial_slowWaveSleep")
    private double initialSlowWaveSleep;
    @Column(name = "current_slowWaveSleep")
    private double currentSlowWaveSleep;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
