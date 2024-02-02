package com.gymapp.gym.profile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private ProfileDto profileDto;
    private String errorMessage;

    public ProfileResponse(ProfileDto profile) {
        this.profileDto = profile;
        this.errorMessage = null;
    }

    public ProfileResponse(Profile profile) {
        this.errorMessage = null;
    }

    public ProfileResponse(String errorMessage) {
        this.profileDto = null;
        this.errorMessage = errorMessage;
    }

}
