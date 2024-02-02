package com.gymapp.gym.userAnalytics;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/user/analytics")
public class UserAnalyticsController {
    @Autowired
    private UserAnalyticsService userAnalyticsService;

    @GetMapping("/allbyuser")
    public ResponseEntity<List<UserAnalyticsDto>> getAllAnalyticsByUser(HttpServletRequest request) {
        List<UserAnalyticsDto> userAnalytics = userAnalyticsService.getAllByUser(request);

        if (userAnalytics == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(userAnalytics);
    }
}
