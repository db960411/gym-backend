package com.gymapp.gym.auth;

import com.gymapp.gym.JWT.JwtService;
import com.gymapp.gym.checkoutToken.CheckoutToken;
import com.gymapp.gym.checkoutToken.CheckoutTokenService;
import com.gymapp.gym.email.EmailService;
import com.gymapp.gym.notifications.Notifications;
import com.gymapp.gym.notifications.NotificationsCategory;
import com.gymapp.gym.notifications.NotificationsService;
import com.gymapp.gym.settings.SettingsService;
import com.gymapp.gym.social.Social;
import com.gymapp.gym.social.SocialService;
import com.gymapp.gym.subscription.SubscriptionService;
import com.gymapp.gym.user.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


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
    @Autowired
    private CheckoutTokenService checkoutTokenService;
    @Autowired
    private UserService userService;

    @Value("${resetPassword.url}")
    private String resetPasswordUrl;

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

    public AuthenticationResponse sendResetPasswordEmail(AuthenticationRequest request) {
        final String email = request.getEmail().toLowerCase();
        Optional<User> user = repository.findUserByEmail(email);

        if (user.isPresent()) {
            try {
                final int verificationCode = checkoutTokenService.getOrCreateCheckoutTokenForUser(user.get(), 15).getToken();
                emailService.sendEmail(user.get().getEmail(), "Password Reset", "If you have requested a password change for this account, please click this link: " + resetPasswordUrl + verificationCode);
                return AuthenticationResponse.builder().successMessage("User password sent to email.").build();
            } catch (MailException e) {
                return AuthenticationResponse.builder().errorMessage("There was an error sending the email.").build();
            }
        }

        return AuthenticationResponse.builder().errorMessage("User not found.").build();
    }


    public AuthenticationResponse updatePassword(AuthenticationPasswordReset request) {
        User user = checkoutTokenService.getUserFromCheckoutToken(request.getTokenId());

        if (user == null) {
            return AuthenticationResponse.builder().errorMessage("Session has expired. Please try again.").build();
        }

        userService.updateUserPassword(user, request.getNewPassword());

        return AuthenticationResponse.builder().successMessage("Password was successfully updated!").build();
    }
}
