package com.gymapp.gym.subscription;

import com.gymapp.gym.email.EmailService;
import com.gymapp.gym.subcriptionEvents.SubscriptionEventService;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SubscriptionEventService subscriptionEventService;

    @Autowired
    private EmailService emailService;

    public Subscription getStatus(HttpServletRequest request) {
         final String email = request.getHeader("Email");
         User user = userService.getUserByEmail(email);

         if (user == null) {
             throw new IllegalArgumentException("User not found");
         }

         Subscription subscription = subscriptionRepository.findByUserId(user.getId());

        return toSubscriptionDto(subscription);
    }

    public void subscribeToPremium(String email) {
        User user = userService.getUserByEmail(email);

        if (subscriptionRepository.existsByUserEmail(user.getEmail())) {
            Subscription subscription = subscriptionRepository.findByUserId(user.getId());

            subscription.setSubscriptionType(SubscriptionType.PREMIUM);
            subscription.setOne_time_payment(true);

            subscriptionEventService.addSubscriptionEvent(user);
            subscriptionRepository.save(subscription);
        }
    }

    public void subscribeToStandard(String email) {
        User user = userService.getUserByEmail(email);

        if (subscriptionRepository.existsByUserEmail(user.getEmail())) {
            Subscription subscription = subscriptionRepository.findByUserId(user.getId());

            subscription.setSubscriptionType(SubscriptionType.STANDARD);
            subscription.setOne_time_payment(true);

            subscriptionEventService.addSubscriptionEvent(user);
            subscriptionRepository.save(subscription);
        }
    }

    public void subscribeToBasic(String email) {
        User user = userService.getUserByEmail(email);

        if (!subscriptionRepository.existsByUserEmail(user.getEmail())) {
            Subscription subscription = new Subscription();

            subscription.setUser(user);
            subscription.setSubscriptionType(SubscriptionType.BASIC);

            subscriptionEventService.addSubscriptionEvent(user);
            subscriptionRepository.save(subscription);
        }
    }

    public void registerEmailVerifiedForUser(User user) {
        if (userService.getUserById(user.getId()) == null) {
            throw new IllegalArgumentException("User doesn't exist when trying to register email verification");
        }
        Subscription subscription = subscriptionRepository.findByUserId(user.getId());
        if (!subscription.isVerified_email()) {
            subscription.setVerified_email(true);
            subscriptionRepository.save(subscription);

            final String emailSubject = "Congratulations, You're Now a Verified Member! 🎉";
            final String emailText = "As a verified member, you can unlock exciting features such as exclusive subscriptions and access to premium content. Start exploring the full range of possibilities today!";


            emailService.addToEmailQueue(user.getEmail(),emailSubject, emailText);
        }
    }

    public Subscription getByUserId(Integer id) {
        return subscriptionRepository.findByUserId(id);
    }

    public Subscription toSubscriptionDto(Subscription subscription) {
        Subscription sub = new Subscription();
        sub.setVerified_email(subscription.isVerified_email());
        sub.setSubscriptionType(subscription.getSubscriptionType());
        sub.setRecurring_payment(subscription.isRecurring_payment());
        sub.setOne_time_payment(subscription.isOne_time_payment());

        return sub;
    }
}
