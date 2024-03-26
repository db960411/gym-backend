package com.gymapp.gym.notifications;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/notifications")
public class NotificationsController {
    @Autowired
    private NotificationsService notificationsService;


    @GetMapping("/getAllByUser")
    public ResponseEntity<List<NotificationsDto>> getAllByUser(HttpServletRequest request) {
        return ResponseEntity.ok(notificationsService.getAllNotificationsByUser(request));
    }

    @PatchMapping("/updateVisibility")
    public ResponseEntity<String> updateVisibility(HttpServletRequest request, @RequestBody List<Notifications> notifications) {
        return notificationsService.updateVisibility(request, notifications);
    }

    @PostMapping("/createNotification")
    public ResponseEntity<String> createNotifications(HttpServletRequest request, @RequestBody NotificationsDto notificationForm) {
        return notificationsService.createGlobalNotification(request, notificationForm);
    }

}
