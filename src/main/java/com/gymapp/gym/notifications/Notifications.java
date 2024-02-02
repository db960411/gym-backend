package com.gymapp.gym.notifications;


import com.gymapp.gym.social.Social;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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
    private Date createdAt;
    private boolean seen;
}
