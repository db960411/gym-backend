package com.gymapp.gym.profile;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ProfileController {

    private final ProfileService service;

    @GetMapping("/profile-status")
    public ResponseEntity<ProfileResponse> checkUserCreatedProfile(HttpServletRequest request) {
        return ResponseEntity.ok(service.profileStatus(request));
    }

    @PostMapping("/create-profile")
    public ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileDto profileForm, HttpServletRequest httpServletRequest) {
            return ResponseEntity.ok(service.createProfile(httpServletRequest, profileForm));
    }
}