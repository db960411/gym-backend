package com.gymapp.gym.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    @MessageMapping("/greet")
    @SendTo("/topic/greetings")
    public String greeting() {
        return "Hello ";
    }
}
