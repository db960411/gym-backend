package com.gymapp.gym.user;

import com.gymapp.gym.JWT.JwtService;
import com.gymapp.gym.social.SocialService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;

    public User getUserByEmail(@NonNull String email) {
        return repo.getUserByEmail(email);
    }

    public UserDto getUserInformation(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = repo.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User can't be null when fetching user information");
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setLevel(user.getLevel());
        userDto.setRole(user.getRole());
        userDto.setProfileImageUrl(user.getProfileImageUrl());

        return userDto;
    }

    public ResponseEntity<String> updateUserImageUrl(HttpServletRequest request, String profileImageUrl) {
        final String email = request.getHeader("Email");
        User user = repo.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User can't be null when fetching user information");
        }

        if (profileImageUrl != null) {
            user.setProfileImageUrl(profileImageUrl);
            repo.save(user);
        }

        return ResponseEntity.ok().build();
    }

    public String updateUserEmail(@NonNull User user, @NonNull String newEmail) {
        User userByEmail = repo.getUserByEmail(newEmail);

        if (userByEmail == null) {
            user.setEmail(newEmail);
            repo.save(user);

            return jwtService.generateToken(user);
        } else {
            return null;
        }
    }


    public void updateUserPassword(@NonNull User user, @NonNull String newPassword) {
        String newEncryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newEncryptedPassword);
        repo.save(user);
    }

    public User getUserById(@NonNull Integer userId) {
        return repo.getReferenceById(userId);
    }


}
