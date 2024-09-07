package com.gymapp.gym.chat;

import com.gymapp.gym.social.Social;
import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue()
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Social sender;

    @Column(name = "sender_status")
    private String senderStatus;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Social receiver;

    @JoinColumn(name = "receiver_status")
    private String receiverStatus;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false, updatable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String type;
}

