package com.gymapp.gym.newsLetter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/newsletter")
public class NewsLetterController {

    @Autowired
    private NewsLetterService service;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUpUserToNewsLetter(@RequestBody String email, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.signUpUserToNewsLetter(email, httpServletRequest));
    }

    @GetMapping("/get-news-letter-status")
    public ResponseEntity<Boolean> getNewsLetterStatusForUser(HttpServletRequest request) throws IllegalAccessException {
        return service.getNewsLetterStatusForUser(request);
    }

}
