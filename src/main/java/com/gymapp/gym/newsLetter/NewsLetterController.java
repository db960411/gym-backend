package com.gymapp.gym.newsLetter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/")
public class NewsLetterController {

    @Autowired
    private NewsLetterService service;

    @PostMapping("newsletter/sign-up")
    public ResponseEntity<?> signUpUserToNewsLetter(@RequestBody String email, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.signUpUserToNewsLetter(email, httpServletRequest));
    }

}
