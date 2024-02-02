package com.gymapp.gym.social.friendshipRequest;

import com.gymapp.gym.social.Social;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipRequest {
    @Id
    @GeneratedValue()
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_social_id")
    private Social sender;

    @ManyToOne
    @JoinColumn(name = "receiver_social_id")
    private Social receiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

}
