package com.gymapp.gym.userAnalytics;

import com.gymapp.gym.exerciseType.ExerciseType;
import com.gymapp.gym.progress.Progress;
import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
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

    private double currentProgressWeight;
    private double initialProgressWeight;
    private double userProgressWeightPercentageIncrease;

    private double currentProgressSets;
    private double initialProgressSets;
    private double userProgressSetsPercentageIncrease;

    private double currentProgressReps;
    private double initialProgressReps;
    private double userProgressRepsPercentageIncrease;

    private double currentUserWeight;
    private double initialUserWeight;
    private double userWeightPercentageIncrease;

    private String exerciseType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



}
