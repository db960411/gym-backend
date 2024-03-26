package com.gymapp.gym.DashboardGraphSummary.SubscriptionSummary;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/subscriptionSummary")
public class SubscriptionSummaryController {
    @Autowired
    private SubscriptionSummaryService subscriptionSummaryService;

    @GetMapping
    public ResponseEntity<List<SubscriptionSummary>> getAllSubscriptionSummary(HttpServletRequest request) {
       return ResponseEntity.ok(subscriptionSummaryService.getAllSubscriptionSummary(request));
    }

}
