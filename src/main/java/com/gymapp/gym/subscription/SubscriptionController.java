package com.gymapp.gym.subscription;

import com.gymapp.gym.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class SubscriptionController {

    @Autowired
    private final SubscriptionService subscriptionService;
    @GetMapping("subscription-state")
    public ResponseEntity<Subscription> getSubscription(HttpServletRequest request) throws UserNotFoundException {
            return ResponseEntity.ok(subscriptionService.getStatus(request));
    }

}
