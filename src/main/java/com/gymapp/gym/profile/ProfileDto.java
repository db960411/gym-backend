package com.gymapp.gym.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String displayName;
    private String height;
    private String weight;
    private String language;
    private String nationality;
    private String gender;
    private LocalDate dateOfBirth;
    private String fitnessGoals;
}
