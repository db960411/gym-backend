package com.gymapp.gym.chat;


import com.gymapp.gym.social.Social;
import com.gymapp.gym.social.SocialRepository;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserRepository;
import com.gymapp.gym.websocket.WSChatHandler;
import com.gymapp.gym.websocket.WSHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SocialRepository socialRepository;
    @Autowired
    private WSHandler wsHandler;
    @Autowired
    private WSChatHandler wsChatHandler;

    public ChatDto handleNewMessageInChat(@RequestHeader("Email") String email, @RequestBody ChatDto chatDto) throws IllegalAccessException, IOException {
        if (email == null || chatDto == null || chatDto.getMessage() == null) {
            throw new IllegalArgumentException("Invalid input: Email or message is null");
        }

        User senderUser = userRepository.getUserByEmail(email);

        if (senderUser == null) {
            throw new IllegalAccessException("Sender not found when sending message");
        }

        Social senderSocial = socialRepository.getByUserId(senderUser.getId());

        if (senderSocial == null) {
            throw new IllegalAccessException("Sender SOCIAL not found when sending message");
        }

        Social receiverSocial = socialRepository.getById(chatDto.getReceiver());

        if (receiverSocial == null) {
            throw new IllegalAccessException("Receiver not found when sending message");
        }

        if (!senderSocial.getFriends().contains(receiverSocial)) {
            throw new IllegalAccessException("Sender and Receiver are not friends");
        }

        Chat chat = Chat.builder()
                .sender(senderSocial)
                .receiver(receiverSocial)
                .message(chatDto.getMessage())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("sent")
                .type("text")
                .receiverStatus(null)
                .senderStatus("sent")
                .build();

        chatRepository.save(chat);

            WebSocketSession session = wsHandler.getUserSessions().get(senderSocial.getId());
            if (session != null && session.isOpen()) {
                wsHandler.handleChatMessage(chatDto);
            } else {
                log.warn("Session for social ID {} is null or not open", senderSocial.getId());
            }

        return toChatDto(chat);
    }


    public List<ChatDto> getAllMessagesByChat(String email, Integer friendSocialId,
                                               int page,
                                               int size)
            throws IllegalAccessException {
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            throw new IllegalAccessException("User not found when trying to fetch all messages");
        }

        Social currentSocial = socialRepository.getByUserId(user.getId());

        if (currentSocial == null) {
            throw new IllegalAccessException("CurrentSocial not found when trying to fetch all messages");
        }

        Social friendSocial = socialRepository.getById(friendSocialId);

        if (friendSocial == null) {
            throw new IllegalAccessException("Friends social not found when trying to fetch all messages");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        Page<Chat> pagedMessages = chatRepository.findAllBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                currentSocial.getId(),
                friendSocial.getId(),
                friendSocial.getId(),
                currentSocial.getId(),
                pageable
        );

        return pagedMessages.stream().map(this::toChatDto).collect(Collectors.toList());
    }

    @Transactional
    public Boolean updateChatMessagesStatuses(String email, ChatDto chatDto) throws IllegalAccessException {
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            throw new IllegalAccessException("User not found when trying to update status for messages");
        }

        List<Chat> allMessagesForChat = chatRepository.findAllBySenderIdAndReceiverIdAndStatus(chatDto.getSender(), chatDto.getReceiver(), "sent");

        if (allMessagesForChat.isEmpty()) {
            return false;
        }

        allMessagesForChat.forEach(i -> i.setReceiverStatus("seen"));

        chatRepository.saveAll(allMessagesForChat);

        wsChatHandler.handleChatReceiverStatus(chatDto.getReceiver(), chatDto.getSender());

        return true;
    }

    private ChatDto toChatDto(Chat chat) {
        if (chat == null) {
            return null;
        }
        ChatDto chatDto = new ChatDto();
        chatDto.setReceiver(chat.getReceiver().getId());
        chatDto.setSender(chat.getSender().getId());
        chatDto.setMessage(chat.getMessage());
        chatDto.setTimestamp(chat.getTimestamp());
        chatDto.setStatus(chat.getStatus());
        chatDto.setType(chat.getType());
        chatDto.setReceiverStatus(chat.getReceiverStatus());
        chatDto.setSenderStatus(chat.getSenderStatus());
        return chatDto;
    }
}
