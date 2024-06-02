package com.gymapp.gym.settings;

import com.gymapp.gym.auth.AuthenticationRequest;
import com.gymapp.gym.auth.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account-settings")
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public ResponseEntity<SettingsDto> settings(HttpServletRequest request) {
        return ResponseEntity.ok(settingsService.getOrCreateSettingsByUser(request));
    }

    @DeleteMapping("/remove-plan")
    public ResponseEntity<SettingsResponse> cancelPlanProgressionForUser(HttpServletRequest request) {
        return ResponseEntity.ok(settingsService.cancelUserPlanprogression(request));
    }

    @PatchMapping("/receive-emails")
    public ResponseEntity<SettingsResponse> receiveEmailsForUser(HttpServletRequest request, @RequestBody boolean selectedValue) {
        return ResponseEntity.ok(settingsService.receiveEmailsByUser(request, selectedValue));
    }

    @PatchMapping("/allow-notifications")
    public ResponseEntity<SettingsResponse> allowNotificationsForUser(HttpServletRequest request, @RequestBody boolean selectedValue) {
        return ResponseEntity.ok(settingsService.allowNotificationsByUser(request, selectedValue));
    }

    @PostMapping("/change-language")
    public ResponseEntity<SettingsResponse> changeLanguage(HttpServletRequest request, @RequestBody String language) {
        if (language == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(settingsService.updateLanguage(request, language));
    }

    @PostMapping("/change-email")
    public ResponseEntity<SettingsResponse> updateEmailAddress(HttpServletRequest request, @RequestBody String newEmail) {

        if (newEmail == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(settingsService.updateEmail(request, newEmail));
    }

    @PostMapping("/change-password")
    public ResponseEntity<SettingsResponse> updatePassword(HttpServletRequest request, @RequestBody String newPassword) {
        if (newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(settingsService.updatePassword(request, newPassword));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<SettingsResponse> verifyEmailAddress(HttpServletRequest request) throws IllegalAccessException {
        return ResponseEntity.ok(settingsService.sendVerificationEmailForUser(request));
    }

    @PatchMapping("/allow-smart-ui")
    public ResponseEntity<SettingsResponse> changeSmartUISettings(HttpServletRequest request, @RequestBody boolean selectedValue) throws IllegalAccessException {
        return ResponseEntity.ok(settingsService.changeSmartUISettings(request, selectedValue));
    }

}
