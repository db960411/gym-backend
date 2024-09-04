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
    @Column(name = "initialReps")
    private double initialReps;
    @Column(name = "currentReps")
    private double currentReps;
    @Column(name = "repsIncrease")
    private double repsIncrease;

    @Column(name = "initialSets")
    private double initialSets;
    @Column(name = "currentSets")
    private double currentSets;
    @Column(name = "setsIncrease")
    private double setsIncrease;

    @Column(name = "initialWeight")
    private double initialWeight;
    @Column(name = "currentWeight")
    private double currentWeight;
    @Column(name = "weightIncrease")
    private double weightIncrease;

    @Column(name = "distance")
    private double distance;
    @Column(name = "time")
    private double time;
    @Column(name = "BPM")
    private double BPM;
    @Column(name = "steps")
    private double steps;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
