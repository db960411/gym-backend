package com.gymapp.gym.notifications;

import com.gymapp.gym.profile.Profile;
import com.gymapp.gym.profile.ProfileService;
import com.gymapp.gym.settings.Settings;
import com.gymapp.gym.settings.SettingsService;
import com.gymapp.gym.social.Social;
import com.gymapp.gym.social.SocialService;
import com.gymapp.gym.user.Role;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import com.gymapp.gym.websockets.WebSocketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private Queue<Notifications> notificationQueue = new LinkedList<>();
    @Autowired
    private SettingsService settingsService;

    public void createNotificationForUserSocial(Social toSocial, Social fromSocial, String title, String text, NotificationsCategory category) {
        if (toSocial == null) {
            throw new IllegalArgumentException("Social object is null");
        }

        toSocial = socialService.getById(toSocial.getId());

        if (toSocial == null) {
            throw new IllegalStateException("Social not found with the provided ID");
        }

        Notifications notification = new Notifications();
        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setSocial(toSocial);
        notification.setText(text);
        notification.setTitle(title);
        notification.setCategory(category);
        notification.setFromSocial(fromSocial);

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
            if (notification.getFromSocial() != null) {
                notificationsDto.setFriendImageUrl(notification.getFromSocial().getUser().getProfileImageUrl());
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
            notifications.setCreatedAt(Date.from(Instant.now()));
            notifications.setSeen(false);
            notifications.setTitle(notificationsDto.getTitle());
            notifications.setText(notificationsDto.getText());
            notifications.setCategory(NotificationsCategory.ADMIN);

            notificationsRepository.save(notifications);
            notificationQueue.offer(notifications);
        });


        return ResponseEntity.ok("Success!");
    }

    public void addNotificationsToFriendlySendOutQueue(User user, String title, NotificationsCategory category, Date createdAt) {
        Social social = socialService.getByUserId(user.getId());

        if (social == null) {
            throw new RuntimeException();
        }

        social.getFriends().forEach(friend -> {
            Notifications notification = new Notifications();
            notification.setFromSocial(social);
            notification.setSocial(friend);
            notification.setTitle(title);
            notification.setCategory(category);
            notification.setCreatedAt(createdAt);
            notificationQueue.offer(notification);
        });
    }

    public void sendNextNotificationInQueue() {
        if (!notificationQueue.isEmpty()) {
            Notifications notification = notificationQueue.peek();
            int maxAttempts = 3;
            int currentAttempt = 0;
            while (currentAttempt < maxAttempts) {
                try {
                    notificationsRepository.save(notification);
                    notificationQueue.poll();
                    log.info("Created notification from queue");
                    break;
                } catch (Exception e) {
                    currentAttempt++;
                    if (currentAttempt >= maxAttempts) {
                        log.warn("Failed to send notification after multiple attempts: " + e.getMessage());
                    } else {
                       log.info("Retry attempt " + currentAttempt + " after failure: " + e.getMessage());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 */2 * * * ?")
    public void sendQueuedEmails() {
        int batchSize = 10;
        for (int i = 0; i < batchSize; i++) {
            if (notificationQueue.isEmpty()) {
                break;
            }
            sendNextNotificationInQueue();
        }
    }



}
