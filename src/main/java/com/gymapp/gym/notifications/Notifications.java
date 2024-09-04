package com.gymapp.gym.notifications;


import com.gymapp.gym.social.Social;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Notifications {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "social_id")
    private Social social;

    @ManyToOne
    @JoinColumn(name = "from_social_id")
    private Social fromSocial;

    private String title;
    private String text;
    @Enumerated(EnumType.STRING)
    private NotificationsCategory category;
    private Timestamp createdAt;
    private boolean seen;
}
