package com.gymapp.gym.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/jwt")
public class JwtController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<Boolean> checkIfUserAuthenticated(@RequestBody JwtDto jwtDto) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtDto.getEmail());
        return ResponseEntity.ok(jwtService.isTokenValid(jwtDto.getToken(), userDetails));
    }

}
