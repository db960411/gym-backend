package com.gymapp.gym.newsLetter;

import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsLetterService {

    private final NewsLetterRepository repository;
    private final UserService userService;

    public ResponseEntity<?> signUpUserToNewsLetter(@NonNull String email, @NonNull HttpServletRequest httpServletRequest) {
        final String authEmail = httpServletRequest.getHeader("Email");

        if (!authEmail.equals(email)) {
            return new ResponseEntity<>("The email provided is wrong. Please contact support", HttpStatus.FORBIDDEN);
        }
        User user = userService.getUserByEmail(email);

        boolean userAlreadySignedUp = repository.existsByUserId(user.getId());

        if (userAlreadySignedUp) {
            return new ResponseEntity<>("The user is already signed to news letter", HttpStatus.FORBIDDEN);
        }

        repository.save(NewsLetter.builder().email(user.getEmail()).user(user).build());

        return new ResponseEntity<>("User signed up to news letter.", HttpStatus.CREATED);
    }

}
