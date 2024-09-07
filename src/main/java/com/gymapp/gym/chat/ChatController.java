package com.gymapp.gym.chat;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/handleNewMessage")
    private ResponseEntity<ChatDto> handleNewMessageInChat(@RequestHeader String email, @RequestBody ChatDto chatDto) throws IllegalAccessException, IOException {
        return ResponseEntity.ok(this.chatService.handleNewMessageInChat(email, chatDto));
    }

    @GetMapping("/getAllMessages/{friendSocialId}")
    private ResponseEntity<List<ChatDto>> handleNewMessageInChat(@RequestHeader String email, @PathVariable Integer friendSocialId, @RequestParam(defaultValue = "0") int page,  @RequestParam(defaultValue = "100") int size) throws IllegalAccessException {
        return ResponseEntity.ok(this.chatService.getAllMessagesByChat(email, friendSocialId, page, size));
    }

    @PatchMapping("/updateChatMessagesStatus")
    private ResponseEntity<Boolean> updateChatMessagesStatuses(@RequestHeader String email, @RequestBody ChatDto chatDto) throws IllegalAccessException, IOException {
        return ResponseEntity.ok(this.chatService.updateChatMessagesStatuses(email, chatDto));
    }

}