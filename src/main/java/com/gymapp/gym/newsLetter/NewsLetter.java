package com.gymapp.gym.newsLetter;

import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Builder
@Table(name = "news_letter")
public class NewsLetter {
    @Id
    @GeneratedValue
    private Integer id;
    private String email;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
