package com.gymapp.gym.dashboard;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;


    @GetMapping("/sendAdminApprovalLink")
    public ResponseEntity<String> sendAdminApprovalLink(HttpServletRequest request) {
        boolean sentEmail = dashboardService.sendApprovalEmailToAdmin(request);

        if (!sentEmail) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok("Sent email successfully.");
    }


    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> authenticateApprovalCode(HttpServletRequest request, @RequestBody int code) {
        boolean authenticated = dashboardService.authenticateApprovalCode(request, code);

        if (authenticated) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
