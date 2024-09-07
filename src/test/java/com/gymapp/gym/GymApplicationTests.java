package com.gymapp.gym;

import com.gymapp.gym.websocket.WSHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
class GymApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testSingletonWebsocketHandler() {
        WSHandler handler1 = context.getBean(WSHandler.class);
        WSHandler handler2 = context.getBean(WSHandler.class);
        assertSame(handler1, handler2);
    }

}
