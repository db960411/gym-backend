package com.gymapp.gym.notifications;

import com.gymapp.gym.notes.NotesDto;
import com.gymapp.gym.profile.Profile;
import com.gymapp.gym.profile.ProfileService;
import com.gymapp.gym.settings.Settings;
import com.gymapp.gym.settings.SettingsService;
import com.gymapp.gym.social.Social;
import com.gymapp.gym.social.SocialService;
import com.gymapp.gym.user.Role;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import com.gymapp.gym.websocket.WSChatHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class NotificationsService {
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private SocialService socialService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;
    private final Queue<SimpleMailMessage> emailQueue = new LinkedList<>();
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private WSChatHandler wsChatHandler;

    public void createNotificationForUserSocial(Social toSocial, Social fromSocial, String title, String text, NotificationsCategory category)  {
        if (toSocial == null) {
            throw new IllegalArgumentException("Social object is null");
        }

        toSocial = socialService.getById(toSocial.getId());

        if (toSocial == null) {
            throw new IllegalStateException("Social not found with the provided ID");
        }

        Notifications notification = new Notifications();
        notification.setCreatedAt(Timestamp.from(Instant.now()));
        notification.setSocial(toSocial);
        notification.setFromSocial(fromSocial);
        notification.setText(text);
        notification.setTitle(title);
        notification.setCategory(category);

        notificationsRepository.save(notification);
    }


    public List<NotificationsDto> getAllNotificationsByUser(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        Settings settings = settingsService.getSettingsByUser(user);

        if (!settings.isAllowNotifications()) {
            return Collections.emptyList();
        }

        Social social = socialService.getByUserId(user.getId());

        if (social == null) {
           return null;
        }

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.asc("seen"), Sort.Order.desc("createdAt")));

        List<Notifications> notifications = notificationsRepository.findAllBySocialId(social.getId(), pageable);
        List<NotificationsDto> notificationsDtoList = new ArrayList<>();

        notifications.forEach(notification -> {
            NotificationsDto notificationsDto = new NotificationsDto();
            notificationsDto.setId(notification.getId());
            notificationsDto.setTitle(notification.getTitle());
            notificationsDto.setText(notification.getText());
            notificationsDto.setCategory(notification.getCategory());
            notificationsDto.setCreatedAt(notification.getCreatedAt());
            notificationsDto.setSeen(notification.isSeen());
            notificationsDto.setFromSocialId(social.getId());

            if (notification.getFromSocial() != null) {
                notificationsDto.setFriendImage(notification.getFromSocial().getUser().getImage());
                notificationsDto.setFriendEmailAdress(notification.getFromSocial().getUser().getEmail());
                Profile profile = profileService.getProfile(notification.getFromSocial().getUser().getEmail());

                if (profile != null) {
                    notificationsDto.setFriendDisplayName(profile.getDisplayName());
                } else {
                    notificationsDto.setFriendDisplayName(notification.getFromSocial().getUser().getEmail());
                }
            }

            notificationsDtoList.add(notificationsDto);
        });

        if (notificationsDtoList.isEmpty()) {
            return Collections.emptyList();
        }

        return notificationsDtoList;
    }

    public ResponseEntity<String> updateVisibility(HttpServletRequest request, List<Notifications> notificationsList) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("No user found when trying to update notifications visibility");
        }

        notificationsList.forEach(nt -> {
          Notifications notifications = notificationsRepository.getReferenceById(nt.getId());
          notifications.setSeen(true);
          notificationsRepository.save(notifications);
        });

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> createGlobalNotification(HttpServletRequest request, NotificationsDto notificationsDto) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);
        Social adminSocial = socialService.getByUserId(user.getId());

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is null or doesn't have required role!");
        }

        List<Social> listOfAllSocials = socialService.getAllSocials();

        listOfAllSocials.forEach(social -> {
            Notifications notifications = new Notifications();
            notifications.setFromSocial(adminSocial);
            notifications.setSocial(social);
            notifications.setCreatedAt(Timestamp.from(Instant.now()));
            notifications.setSeen(false);
            notifications.setTitle(notificationsDto.getTitle());
            notifications.setText(notificationsDto.getText());
            notifications.setCategory(NotificationsCategory.ADMIN);
            notificationsRepository.save(notifications);

            sendWSNotification(adminSocial, social, toDto(notifications, social, "FITSYNC"));
        });


        return ResponseEntity.ok("Success!");
    }

    public void sendNotificationsToSocialsFriends(User user, @Nullable String profileDisplayName, String title, NotificationsCategory category, Timestamp createdAt) {
        Social social = socialService.getByUserId(user.getId());

        if (social == null) {
            throw new RuntimeException();
        }

        social.getFriends().forEach(friendSocial -> {
            Notifications notification = new Notifications();
            notification.setFromSocial(social);
            notification.setSocial(friendSocial);
            notification.setTitle(title);
            notification.setCategory(category);
            notification.setCreatedAt(createdAt);
            notificationsRepository.save(notification);

            sendWSNotification(social, friendSocial, toDto(notification, friendSocial, profileDisplayName));
        });
    }

    public void sendWSNotification(Social social, Social friend, NotificationsDto notificationDto) {
            wsChatHandler.handleNotification(social, friend, notificationDto);
            log.warn("Session for social ID {} is null or not open", social.getId());
    }

    public void sendNextEmailInQueue() {
        if (!emailQueue.isEmpty()) {
            int maxAttempts = 3;
            int currentAttempt = 0;
            while (currentAttempt < maxAttempts) {
                try {
                    emailQueue.poll();
                    log.info("Created email from queue");
                    break;
                } catch (Exception e) {
                    currentAttempt++;
                    if (currentAttempt >= maxAttempts) {
                        log.warn("Failed to send email after multiple attempts: " + e.getMessage());
                    } else {
                       log.info("Retry attempt " + currentAttempt + " after failure: " + e.getMessage());
                            try {
                                Thread.sleep(1000 * currentAttempt);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                    }
                }
            }
        }
    }

    public NotificationsDto toDto(Notifications notifications, Social friendSocial, String friendProfileDisplayName) {
        NotificationsDto dto = new NotificationsDto();
        dto.setId(notifications.getId());
        dto.setFriendImage(friendSocial.getUser().getImage());
        if (friendProfileDisplayName != null) {
            dto.setFriendDisplayName(friendProfileDisplayName);
        }
        dto.setFriendImage(friendSocial.getUser().getImage());
        dto.setTitle(notifications.getTitle());
        dto.setText(notifications.getText());
        dto.setCategory(notifications.getCategory());
        dto.setCreatedAt(notifications.getCreatedAt());
        dto.setSeen(notifications.isSeen());
        dto.setFriendImage(notifications.getSocial().getUser().getImage());
        dto.setFriendEmailAdress(notifications.getSocial().getUser().getEmail());
        return dto;
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void sendQueuedEmails() {
        int batchSize = 10;
        for (int i = 0; i < batchSize; i++) {
            if (emailQueue.isEmpty()) {
                break;
            }
            sendNextEmailInQueue();
        }
    }



}
