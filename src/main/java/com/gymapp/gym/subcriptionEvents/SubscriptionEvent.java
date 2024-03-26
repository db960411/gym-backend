package com.gymapp.gym.subcriptionEvents;

import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table
public class SubscriptionEvent {
    @Id
    @GeneratedValue
    private UUID id;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
