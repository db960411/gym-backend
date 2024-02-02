package com.gymapp.gym.social;


import com.gymapp.gym.plans.plan_progression.PlanProgressionDto;
import com.gymapp.gym.profile.ProfileDto;
import com.gymapp.gym.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialFriendsDto {
    private int userSocialId;
    private UserDto userInfo;
    private String errorMessage;
    private PlanProgressionDto planProgressionDto;
    private ProfileDto profileDto;
}
