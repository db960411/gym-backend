package com.gymapp.gym.config;

import com.gymapp.gym.websocket.WSChatHandler;
import com.gymapp.gym.websocket.WSHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public WSHandler wsHandler() {
        return new WSHandler();
    }

    @Bean
    public WSChatHandler wsChatHandler() {
        return new WSChatHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsHandler(), "/api/v1/ws/topic")
                .setAllowedOrigins("*");
        registry.addHandler(wsChatHandler(), "/api/v1/ws/chat")
                .setAllowedOrigins("*");
    }
}