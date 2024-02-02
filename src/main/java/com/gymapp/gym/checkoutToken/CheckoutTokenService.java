package com.gymapp.gym.checkoutToken;

import com.gymapp.gym.subscription.Subscription;
import com.gymapp.gym.subscription.SubscriptionService;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class CheckoutTokenService {
    @Autowired
    private UserService userService;
    @Autowired
    private CheckoutTokenRepository repository;
    @Autowired
    private SubscriptionService subscriptionService;

    public CheckoutTokenDto createCheckoutTokenForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        CheckoutToken checkoutToken = new CheckoutToken();
        checkoutToken.setUser(user);
        checkoutToken.setCreatedAt(LocalDateTime.now());
        checkoutToken.setExpiresAt(checkoutToken.getCreatedAt().plusHours(12));
        checkoutToken.setToken(authenticationCode());

        repository.save(checkoutToken);

        return CheckoutTokenDto.builder().token(checkoutToken.getToken()).build();
    }


    public CheckoutTokenDto verifyCheckoutByToken(HttpServletRequest request, int token) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("No user found");
        }

        CheckoutToken checkoutToken = repository.getByToken(token);

        if (checkoutToken != null && checkoutToken.getToken() == token) {
            if (checkoutToken.getExpiresAt().isAfter(LocalDateTime.now())) {
                    Subscription subscription = subscriptionService.getByUserId(user.getId());
                    if (subscription != null) {
                        subscriptionService.registerEmailVerifiedForUser(user);
                    } else {
                      throw new IllegalArgumentException("User has no subscription");
                    }

            } else {
                return CheckoutTokenDto.builder().errorMessage("Token has expired, please try to verify your email again.").build();
            }
        }

        return CheckoutTokenDto.builder().success(true).build();
    }


    public int authenticationCode() {
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }
}
