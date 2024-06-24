package com.gymapp.gym.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshRequest {
    String refreshToken;
    String email;
}
