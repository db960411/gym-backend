package com.gymapp.gym.websockets;

import com.gymapp.gym.notifications.Notifications;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(Notifications notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
