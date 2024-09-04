package com.gymapp.gym.analytics.UserAnalytics;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/api/v1/user/analytics")
public class UserAnalyticsController {
    @Autowired
    private UserAnalyticsService userAnalyticsService;

    @GetMapping("/allUserAnalytics")
    public ResponseEntity<List<UserAnalyticsDto>> getAllAnalyticsByUser(HttpServletRequest request) {
        List<UserAnalyticsDto> userAnalytics = userAnalyticsService.getAllByUser(request);

        return ResponseEntity.ok(userAnalytics);
    }
    @PostMapping
    public ResponseEntity<List<UserAnalyticsDto>> updateAnalyticsForUser(HttpServletRequest request, @RequestBody UserAnalyticsDto userAnalyticsDto) {
        return ResponseEntity.ok(userAnalyticsService.updateUserAnalyticsForUser(request, userAnalyticsDto));
    }
}
