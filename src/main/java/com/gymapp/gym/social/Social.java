package com.gymapp.gym.social;

import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Social {
    @Id
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "social_friends",
            joinColumns = @JoinColumn(name = "social_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_social_id")
    )
    private Set<Social> friends = new HashSet<>();


}
