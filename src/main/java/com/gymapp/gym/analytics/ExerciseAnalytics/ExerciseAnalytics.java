package com.gymapp.gym.analytics.ExerciseAnalytics;

import com.gymapp.gym.exerciseType.ExerciseType;
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
public class ExerciseAnalytics {
    @Id
    @GeneratedValue
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;
    @ManyToOne
    @JoinColumn(name = "exercise_type_id")
    private ExerciseType exerciseType;
    private String exerciseTypeName;
    @Column(name = "initialReps")
    private double initialReps;
    @Column(name = "currentReps")
    private double currentReps;
    @Column(name = "repsPercentageIncrease")
    private double repsPercentageIncrease;

    @Column(name = "initialSets")
    private double initialSets;
    @Column(name = "currentSets")
    private double currentSets;
    @Column(name = "setsPercentageIncrease")
    private double setsPercentageIncrease;

    @Column(name = "initialWeight")
    private double initialWeight;
    @Column(name = "currentWeight")
    private double currentWeight;
    @Column(name = "weightPercentageIncrease")
    private double weightPercentageIncrease;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
