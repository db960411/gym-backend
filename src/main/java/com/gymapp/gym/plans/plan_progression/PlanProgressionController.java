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

    @PostMapping("updatePlanProgressionDay")
    public ResponseEntity<PlanProgressionDto> updatePlanProgressionDay(HttpServletRequest request, @RequestBody boolean previousDay) {
        return planProgressionService.updatePlanProgressionDay(request, previousDay);
    }

    @DeleteMapping("cancelPlanProgressionByUser")
    public ResponseEntity<PlanProgressionDto> cancelPlanProgressionByUser(HttpServletRequest request) {
        return planProgressionService.cancelPlanProgressionByUser(request);
    }
}
