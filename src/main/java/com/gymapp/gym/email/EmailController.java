package com.gymapp.gym.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/send-email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        if (to == null || subject == null || text == null ){
            return "Missing parameters to send email";
        }
        emailService.sendEmail(to, subject, text);
        return "Email sent correctly";
    }

}
