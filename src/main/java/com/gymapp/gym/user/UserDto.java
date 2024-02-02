package com.gymapp.gym.user;



import com.gymapp.gym.profile.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private Role role;
    private Level level;
    private int socialId;
    private ProfileDto profileDto;
    private String profileImageUrl;
}
