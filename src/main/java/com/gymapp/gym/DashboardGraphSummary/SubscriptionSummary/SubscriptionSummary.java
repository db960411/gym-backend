package com.gymapp.gym.DashboardGraphSummary.SubscriptionSummary;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class SubscriptionSummary {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime createdAt;
    private Integer week;
    private String name;
    private int value;
}
