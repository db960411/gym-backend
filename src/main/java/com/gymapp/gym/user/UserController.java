package com.gymapp.gym.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserInformation")
    public ResponseEntity<UserDto> getUserInformation(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getUserInformation(request));
    }
}
