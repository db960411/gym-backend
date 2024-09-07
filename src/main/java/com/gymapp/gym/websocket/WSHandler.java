package com.gymapp.gym.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymapp.gym.chat.ChatDto;
import com.gymapp.gym.notifications.NotificationsDto;
import com.gymapp.gym.social.Social;
import com.gymapp.gym.social.friendshipRequest.FriendShipWSResponse;
import com.gymapp.gym.user.UserDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WSHandler extends TextWebSocketHandler {
    @Getter
    private final Map<Integer, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        int socialId = extractSocialIdFromSession(session);

        if (socialId != 0) {
            userSessions.put(socialId, session);
            log.info("Session added for social ID {}. Current sessions: {}", socialId, userSessions.keySet());

            if (session.isOpen()) {
                notifyUsersAboutOnline(socialId);
            }
        } else {
            log.warn("Social ID could not be extracted from the session.");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (findSocialIdBySession(session)){
            int socialId = extractSocialIdFromSession(session);

            userSessions.remove(socialId);
            log.info("Session removed for user ID {}. Current sessions: {}", socialId, userSessions.keySet());

            notifyUsersAboutDisconnection(socialId);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received message: " + payload);

        if (payload.contains("checkFriendsOnline")) {
            checkUsersOnlineStatus(session, payload);
            return;
        }

        if ("ping".equals(payload)) {
            session.sendMessage(new TextMessage("pong"));
            log.info("Sent pong in response to ping");
        }
    }

    public void handleChatMessage(ChatDto chatDto) throws IOException {
        Integer senderId = chatDto.getSender();
        Integer recipientId = chatDto.getReceiver();

        WebSocketSession recipientSession = userSessions.get(recipientId);

        if (recipientSession != null && recipientSession.isOpen()) {
            chatDto.setReceiverOnline(true);
            chatDto.setTimestamp(new Timestamp(System.currentTimeMillis()));
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatDto)));
            log.info("Sent chat message to from social ID {}: to{} data: {}", senderId, recipientId, chatDto);
        } else {
            log.warn("Recipient user ID {} is not connected", recipientId);
        }
    }

    public void handleNewFriendRequest(UserDto senderDto, int recipientSocialId) throws IOException {
        Integer senderId = senderDto.getSocialId();
        Integer recipientId = recipientSocialId;

        WebSocketSession recipientSession = userSessions.get(recipientId);

        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(senderDto)));
            log.info("Sent friend request from senderId {}: to {}", senderId, recipientId);
        } else {
            log.warn("Recipient user ID {} is not connected", recipientId);
        }
    }

    public void handleNotification(Social fromSocial, Social friend, NotificationsDto notificationsDto) {
        if (fromSocial == null || friend == null || notificationsDto == null) {
            log.error("Null parameter(s) passed to handleNotification");
            return;
        }

        Integer senderId = fromSocial.getId();

        WebSocketSession recipientSession = userSessions.get(friend.getId());

        if (recipientSession != null && recipientSession.isOpen()) {
            try {
                recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(notificationsDto)));
                log.info("Sent notification from social ID {} to recipient ID {}: data: {}", senderId, friend.getId(), notificationsDto);
            } catch (IOException e) {
                log.error("Failed to send notification from social ID {} to recipient ID {}: {}", senderId, friend.getId(), e.getMessage());
            }
        } else {
            log.warn("Recipient user ID {} is not connected or session is not open", friend.getId());
        }
    }

    private Integer extractSocialIdFromSession(WebSocketSession session) {
       String socialId = Objects.requireNonNull(session.getUri()).getQuery();
       return Integer.valueOf(socialId);
    }

    private boolean findSocialIdBySession(WebSocketSession session) {
        // Find user ID by session
        return userSessions.containsValue(session);
    }

    private void checkUsersOnlineStatus(WebSocketSession session, String payload) throws IOException {
        JsonNode rootNode = objectMapper.readTree(payload);
        JsonNode friendsNode = rootNode.get("checkFriendsOnline");

        List<Integer> listOfOnlineSocialIds = new ArrayList<>();

        if (friendsNode.isArray()) {
            // Iterate over the array elements
            Iterator<JsonNode> elements = friendsNode.elements();
            while (elements.hasNext()) {
                JsonNode idNode = elements.next();
                int socialId = idNode.asInt();

                WebSocketSession recipientSession = userSessions.get(socialId);
                if (recipientSession != null && recipientSession.isOpen()) {
                    listOfOnlineSocialIds.add(socialId);
                }
            }
        }

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(listOfOnlineSocialIds)));
        log.info("Sent list of online social IDS in response to checkFriendsOnline");
    }
    private void notifyUsersAboutOnline(int socialId) throws IOException {
        String message = "Online: " + socialId;

        TextMessage notificationMessage = new TextMessage(message);

        for (WebSocketSession activeSession : userSessions.values()) {
            if (activeSession.isOpen()) {
                activeSession.sendMessage(notificationMessage);
            }
        }

        log.info("Notified users of user ID has come online {}", socialId);
    }

    private void notifyUsersAboutDisconnection(int socialId) throws IOException {
        String message = "Offline: " + socialId;

        TextMessage notificationMessage = new TextMessage(message);

        for (WebSocketSession activeSession : userSessions.values()) {
            if (activeSession.isOpen()) {
                activeSession.sendMessage(notificationMessage);
            }
        }

        log.info("Notified remaining users about the disconnection of user ID {}", socialId);
    }

}
