package com.gymapp.gym.DashboardGraphSummary.SubscriptionSummary;

import com.gymapp.gym.user.Role;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionSummaryService {
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionSummaryRepository repository;

    public List<SubscriptionSummary> getAllSubscriptionSummary(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found when fetching subscription summary");
        }

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User not admin");
        }

       return repository.findAll();
    }

}
