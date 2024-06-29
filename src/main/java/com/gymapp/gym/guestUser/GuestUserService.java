package com.gymapp.gym.guestUser;

import com.gymapp.gym.JWT.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GuestUserService {
    @Autowired
    private JwtService jwtService;

    public String generateTokenForGuest(String username) throws UsernameNotFoundException {
        if (username.equals("guest")) {
           return jwtService.generateToken(User.withUsername("guest")
                    .password("")  // No password required for guest
                    .roles("GUEST")
                    .build());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
