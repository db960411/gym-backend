package com.gymapp.gym.chat;

import com.gymapp.gym.social.Social;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChatDto {
    private int sender;
    private String senderStatus;
    private int receiver;
    private String receiverStatus;
    private String message;
    private Timestamp timestamp;
    private String status;
    private String type;
    private boolean receiverOnline;
}
