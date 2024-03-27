package com.gymapp.gym.auth;

import com.gymapp.gym.JWT.JwtService;
import com.gymapp.gym.email.EmailService;
import com.gymapp.gym.notifications.Notifications;
import com.gymapp.gym.notifications.NotificationsCategory;
import com.gymapp.gym.notifications.NotificationsService;
import com.gymapp.gym.settings.SettingsService;
import com.gymapp.gym.social.Social;
import com.gymapp.gym.social.SocialService;
import com.gymapp.gym.subscription.SubscriptionService;
import com.gymapp.gym.user.Level;
import com.gymapp.gym.user.Role;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final SubscriptionService subscriptionService;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final SocialService socialService;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private SettingsService settingsService;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder().email(request.getEmail().toLowerCase()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER).level(Level.BRONZE).createdAt(LocalDateTime.now()).build();

        if (repository.findUserByEmail(user.getEmail()).isPresent()) {
            return AuthenticationResponse.builder().emailError("This email is already taken, please try another email").build();
        }

        repository.save(user);
        subscriptionService.subscribeToBasic(user.getEmail());
        settingsService.createSettingsForUser(user);

        String welcomeText = "Welcome aboard! 🌟 We're absolutely delighted to have you here! 🥳";
        String welcomeSubject = "Welcome to gym planet!🥳";

        emailService.addToEmailQueue(user.getEmail(), welcomeSubject, welcomeText);
        socialService.createSocialForUser(user);
        Social social = socialService.getByUserId(user.getId());

        notificationsService.createNotificationForUserSocial(social, null, "Hello and welcome!", "Please check your email! 👋", NotificationsCategory.ADMIN);

        return AuthenticationResponse.builder().successMessage("Registered user successfully").email(user.getEmail()).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(),
                            request.getPassword()
                    )
            );
            var user = repository.findUserByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).email(user.getEmail()).successMessage("Authenticated").build();
        } catch (AuthenticationException ex) {
            return AuthenticationResponse.builder().errorMessage("Authentication failed").build();
        }
    }


}
