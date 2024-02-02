package com.gymapp.gym.checkoutToken;

import com.gymapp.gym.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutTokenDto {
    private UserDto userDto;
    private int token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean success;
    private boolean error;
    private String errorMessage;
}
