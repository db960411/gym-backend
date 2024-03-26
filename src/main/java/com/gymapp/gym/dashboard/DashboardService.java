package com.gymapp.gym.dashboard;


import com.gymapp.gym.auth.AuthenticationService;
import com.gymapp.gym.checkoutToken.CheckoutTokenService;
import com.gymapp.gym.dashboardToken.DashboardTokenService;
import com.gymapp.gym.email.EmailService;
import com.gymapp.gym.user.Role;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private DashboardTokenService dashboardTokenService;

    public boolean sendApprovalEmailToAdmin(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User is null.");
        }

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not admin.");
        }

        int dashboardToken = dashboardTokenService.createDashboardTokenForUser(user);

        try {
           // emailService.sendEmail(user.getEmail(), "Your admin dashboard code " + dashboardToken, "Please use this code to login.");
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Something went wrong when sending email");
        }

        return true;
    }


    public boolean authenticateApprovalCode(HttpServletRequest request, Integer code) {
        if (code == null || Integer.toString(code).length() != 4) {
            throw new RuntimeException("Code has invalid length");
        }

        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found when trying to authenticate through admin dashboard.");
        }

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User doesn't meet requirements to authenticate for dashboard.");
        }

        return dashboardTokenService.verifyDashboardAccessByToken(request, code);
    }
}
