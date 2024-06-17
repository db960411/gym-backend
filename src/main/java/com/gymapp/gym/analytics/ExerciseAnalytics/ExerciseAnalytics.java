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
    private Double initialReps;
    @Column(name = "currentReps")
    private Double currentReps;
    @Column(name = "repsPercentageIncrease")
    private Double repsPercentageIncrease;

    @Column(name = "initialSets")
    private Double initialSets;
    @Column(name = "currentSets")
    private Double currentSets;
    @Column(name = "setsPercentageIncrease")
    private Double setsPercentageIncrease;

    @Column(name = "initialWeight")
    private Double initialWeight;
    @Column(name = "currentWeight")
    private Double currentWeight;
    @Column(name = "weightPercentageIncrease")
    private Double weightPercentageIncrease;

    @Column(name = "distance")
    private Double distance;
    @Column(name = "time")
    private Double time;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
