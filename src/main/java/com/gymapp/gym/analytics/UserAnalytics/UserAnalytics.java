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
    @Column(name = "weightPercentageIncrease")
    private double weightPercentageIncrease;
    @Column(name = "currentBodyFatPercentage")
    private double currentBodyFatPercentage;
    @Column(name = "initialBodyFatPercentage")
    private double initialBodyFatPercentage;
    @Column(name = "bodyFatPercentageIncrease")
    private double bodyFatPercentageIncrease;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
