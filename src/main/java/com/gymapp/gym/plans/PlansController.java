package com.gymapp.gym.plans;

import com.gymapp.gym.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/plans")
public class PlansController {

    @Autowired
    private PlansService service;

    @PostMapping("assignPlanToUser")
    public ResponseEntity<PlansResponse> assignPlanToUser(HttpServletRequest request, @RequestBody String selectedPlan) throws UserNotFoundException {
        return ResponseEntity.ok(service.assignPlanToUser(request, selectedPlan));
    }

    @GetMapping("checkPlanStatusForUser")
    public ResponseEntity<PlansResponse> getPlanStatusForUser(HttpServletRequest request) throws UserNotFoundException {
        return ResponseEntity.ok(service.checkPlanStatusForUser(request));
    }

    @GetMapping("completePlanForUser")
    public ResponseEntity<PlansResponse> completePlanForUser(HttpServletRequest request) {
        return ResponseEntity.ok(service.completePlanForUser(request));
    }

}
