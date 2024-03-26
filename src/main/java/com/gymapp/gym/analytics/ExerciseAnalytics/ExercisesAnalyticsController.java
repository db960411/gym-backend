package com.gymapp.gym.analytics.ExerciseAnalytics;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/v1/exercises/analytics")
public class ExercisesAnalyticsController {
    @Autowired
    private ExerciseAnalyticsService exerciseAnalyticsService;

    @GetMapping("/allByUser")
    public ResponseEntity<List<ExerciseAnalyticsDto>> getAllByUser(HttpServletRequest request) {
        return ResponseEntity.ok(exerciseAnalyticsService.getAllByUser(request));
    }
}
