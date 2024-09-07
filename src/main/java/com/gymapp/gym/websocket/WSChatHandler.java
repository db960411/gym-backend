package com.gymapp.gym.websocket;

import com.gymapp.gym.chat.ChatDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WSChatHandler extends TextWebSocketHandler {
    private final Map<Integer, WebSocketSession> userSessionsInChats = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        int socialId = extractSocialIdFromSession(session);

        if (socialId != 0) {
            userSessionsInChats.put(socialId, session);
            log.info("CHAT session added for social ID {}. Current sessions: {}", socialId, userSessionsInChats.keySet());
        } else {
            log.warn("Social ID could not be extracted from the session.");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        if (findSocialIdBySession(session)){
            int socialId = extractSocialIdFromSession(session);

            userSessionsInChats.remove(socialId);
            log.info("Chat session removed for user ID {}. Current sessions: {}", socialId, userSessionsInChats.keySet());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received message: " + payload);

        if ("ping".equals(payload)) {
            session.sendMessage(new TextMessage("pong"));
            log.info("Sent pong in response to ping");
        }
    }

    public void handleChatReceiverStatus(int reciever, int sender) {
        WebSocketSession recipientSession = userSessionsInChats.get(reciever);
        WebSocketSession senderSession = userSessionsInChats.get(sender);

        if (recipientSession != null && recipientSession.isOpen() && senderSession != null && senderSession.isOpen()) {
            try {
                senderSession.sendMessage(new TextMessage("ChatMessagesSeen"));
            } catch (IOException e) {
                log.error("Failed to send chatMessagesSeen", e.getMessage());
            }
        }

        log.info("Sent list of online social IDS in response to checkFriendsOnline");
    }


    private Integer extractSocialIdFromSession(WebSocketSession session) {
        String socialId = Objects.requireNonNull(session.getUri()).getQuery();
        return Integer.valueOf(socialId);
    }

    private boolean findSocialIdBySession(WebSocketSession session) {
        return userSessionsInChats.containsValue(session);
    }

}
