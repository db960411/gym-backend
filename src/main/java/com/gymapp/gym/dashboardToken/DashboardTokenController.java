package com.gymapp.gym.dashboardToken;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardTokenController {
    @Autowired
    private DashboardTokenService checkoutTokenService;

    @PostMapping("/verify-token")
    public ResponseEntity<String> verifyDashboardTokenById(HttpServletRequest request, @RequestBody Integer tokenId) {
        boolean verifiedDashboardAccessByToken = checkoutTokenService.verifyDashboardAccessByToken(request, tokenId);

        if (!verifiedDashboardAccessByToken) {
            return ResponseEntity.ok("failure");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


}
