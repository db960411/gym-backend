package com.gymapp.gym.dashboardToken;

import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class DashboardToken {
    @GeneratedValue
    @Id
    private UUID id;

    @OneToOne
    private User user;

    private int token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
