package com.gymapp.gym.guestUser;

import com.gymapp.gym.auth.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestUserController {
    @Autowired
    private GuestUserService guestUserService;

    @GetMapping("/login")
    public AuthenticationResponse loginAsGuest() {
        String token = guestUserService.generateTokenForGuest("guest");

        if (token != null) {
            return AuthenticationResponse.builder().successMessage("Succesfully logged in as guest").token(token).build();
        }

        return AuthenticationResponse.builder().errorMessage("There was an error.").build();
    }
}
