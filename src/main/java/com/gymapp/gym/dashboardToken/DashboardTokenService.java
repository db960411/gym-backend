package com.gymapp.gym.dashboardToken;

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
public class DashboardTokenService {
    @Autowired
    private UserService userService;
    @Autowired
    private DashboardTokenRepository repository;
    @Autowired
    private SubscriptionService subscriptionService;

    public int createDashboardTokenForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        DashboardToken dashboardToken = new DashboardToken();
        dashboardToken.setUser(user);
        dashboardToken.setCreatedAt(LocalDateTime.now());
        dashboardToken.setExpiresAt(dashboardToken.getCreatedAt().plusMinutes(10));
        dashboardToken.setToken(authenticationCode());

        repository.save(dashboardToken);

        return dashboardToken.getToken();
    }

    public boolean verifyDashboardAccessByToken(HttpServletRequest request, int token) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("No user found");
        }

        DashboardToken dashboardToken = repository.getByToken(token);

        if (dashboardToken != null && dashboardToken.getToken() == token) {
            return dashboardToken.getExpiresAt().isAfter(LocalDateTime.now());
        }

        return false;
    }

    public int authenticationCode() {
        Random random = new Random();
       return random.nextInt(9000) + 1000;
    }
}
