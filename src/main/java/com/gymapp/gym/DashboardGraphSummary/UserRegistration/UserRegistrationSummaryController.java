package com.gymapp.gym.DashboardGraphSummary.UserRegistration;

import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/userRegistrationSummary")
public class UserRegistrationSummaryController {
    @Autowired
    private UserRegistrationSummaryRepository userRegistrationSummaryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AllUsersSummaryRepository allUsersSummaryRepository;

    @GetMapping()
    public ResponseEntity<List<UserRegistrationSummary>> getAllUserRegistrationSummary(HttpServletRequest request) {
        return ResponseEntity.ok(userRegistrationSummaryRepository.findAll());
    }

    @GetMapping("/countAllUsers")
    public ResponseEntity<List<AllUsersSummary>> countAllUsers() {
        return ResponseEntity.ok(allUsersSummaryRepository.findAll());
    }

}
