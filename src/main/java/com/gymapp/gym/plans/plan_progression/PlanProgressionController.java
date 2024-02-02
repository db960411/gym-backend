package com.gymapp.gym.plans.plan_progression;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/planProgression/")
public class PlanProgressionController {
    @Autowired
    private PlanProgressionService planProgressionService;

    @GetMapping("updatePlanProgressionDay")
    public ResponseEntity<PlanProgressionDto> updatePlanProgressionDay(HttpServletRequest request) {
        return planProgressionService.updatePlanProgressionDay(request);
    }

    @DeleteMapping("cancelPlanProgressionByUser")
    public ResponseEntity<PlanProgressionDto> cancelPlanProgressionByUser(HttpServletRequest request) {
        return planProgressionService.cancelPlanProgressionByUser(request);
    }
}
