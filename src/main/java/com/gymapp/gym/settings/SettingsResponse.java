package com.gymapp.gym.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettingsResponse {
    private String errorMessage;
    private String successMessage;
    private String jwtToken;
    private String userLanguage;

    public SettingsResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
