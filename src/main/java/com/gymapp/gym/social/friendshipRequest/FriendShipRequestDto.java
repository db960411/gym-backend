package com.gymapp.gym.social.friendshipRequest;

import com.gymapp.gym.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendShipRequestDto {
    private Set<UserDto> userInfo;
}
