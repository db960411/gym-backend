package com.gymapp.gym.notifications;

import com.gymapp.gym.fileUpload.Image;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class NotificationsDto {
    private UUID id;
    private String title;
    private String text;
    private NotificationsCategory category;
    private Timestamp createdAt;
    private boolean seen;
    private Image friendImage;
    private String friendDisplayName;
    private String friendEmailAdress;
}
