package com.gymapp.gym.settings;

import com.gymapp.gym.profile.Profile;
import com.gymapp.gym.subscription.Subscription;
import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "settings")
public class Settings {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    User user;

    @OneToOne
    Subscription subscription;

    @OneToOne
    Profile profile;

    private String language;

    private boolean allowNotifications;

    private boolean receiveEmails;

    @Column(name = "smart_ui")
    private boolean smartUI;

}
