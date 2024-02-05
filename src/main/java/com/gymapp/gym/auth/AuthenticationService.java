package com.gymapp.gym.auth;

import com.gymapp.gym.JWT.JwtService;
import com.gymapp.gym.email.EmailService;
import com.gymapp.gym.notes.NotesService;
import com.gymapp.gym.social.SocialService;
import com.gymapp.gym.subscription.SubscriptionService;
import com.gymapp.gym.user.Level;
import com.gymapp.gym.user.Role;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserRepository;
import com.gymapp.gym.userAnalytics.UserAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;


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

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder().email(request.getEmail().toLowerCase()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER).level(Level.BRONZE).build();

        if (repository.findUserByEmail(user.getEmail()).isPresent()) {
            return AuthenticationResponse.builder().emailError("This email is already taken, please try another email").build();
        }

        repository.save(user);
        subscriptionService.subscribeToBasic(user.getEmail());

        String welcomeText = "Welcome aboard! 🌟 We're absolutely delighted to have you here! 🥳";
        String welcomeSubject = "Welcome to gym planet!🥳";

        emailService.addToEmailQueue(user.getEmail(), welcomeSubject, welcomeText);
        socialService.createSocialForUser(user);

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
