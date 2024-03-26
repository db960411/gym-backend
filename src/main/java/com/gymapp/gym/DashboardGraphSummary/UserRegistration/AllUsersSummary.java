package com.gymapp.gym.DashboardGraphSummary.UserRegistration;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class AllUsersSummary {
    @Id
    @GeneratedValue
    private UUID id;

    private Integer week;
    private Integer amount;
    private LocalDateTime createdAt;
}
