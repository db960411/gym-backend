package com.gymapp.gym.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class NotificationsDto {
    private UUID id;
    private String title;
    private String text;
    private NotificationsCategory category;
    private Date createdAt;
    private boolean seen;
    private String friendImageUrl;
    private String friendDisplayName;
    private String friendEmailAdress;
}
