package com.gymapp.gym.social;

import com.gymapp.gym.plans.plan_progression.PlanProgressionDto;
import com.gymapp.gym.profile.ProfileDto;
import com.gymapp.gym.social.friendshipRequest.FriendShipRequestDto;
import com.gymapp.gym.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialDto {
    private Set<SocialFriendsDto> friends;
    private int userSocialId;
    private UserDto userInfo;
    private ProfileDto profileInfo;
    private String errorMessage;
    private PlanProgressionDto planProgressionDto;
    private FriendShipRequestDto friendRequests;
}


