package com.gymapp.gym.user;

import com.gymapp.gym.JWT.JwtService;
import com.gymapp.gym.fileUpload.Image;
import com.gymapp.gym.profile.Profile;
import com.gymapp.gym.profile.ProfileDto;
import com.gymapp.gym.profile.ProfileService;
import com.gymapp.gym.social.SocialService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private SocialService socialService;

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
        userDto.setImage(user.getImage());
        userDto.setSocialId(socialService.getByUserId(user.getId()).getId());

        Profile profile = profileService.getByUserId(user.getId());
        if (profile != null) {
            ProfileDto profileDto = new ProfileDto();
            profileDto.setDisplayName(profile.getDisplayName());
            profileDto.setNationality(profile.getNationality());
            profileDto.setGender(profile.getGender());
            profileDto.setHeight(profile.getHeight());
            profileDto.setWeight(profile.getWeight());
            profileDto.setFitnessGoals(profile.getFitnessGoals());
            profileDto.setLanguage(profile.getLanguage());
            profileDto.setDateOfBirth(profile.getDateOfBirth());
            userDto.setProfileDto(profileDto);
        }

        return userDto;
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

    public void saveUserImage(User user) {
        if (user != null) {
            this.repo.save(user);
        }
    }

    public void determineLevelForUser(User user) {
        if (user == null) {
            throw new RuntimeException("User is null when trying to determine user level");
        }

        switch (user.getLevel()) {
            case BRONZE -> user.setLevel(Level.SILVER);
            case SILVER -> user.setLevel(Level.GOLD);
            case GOLD -> user.setLevel(Level.PLATINUM);
            case PLATINUM -> user.setLevel(Level.DIAMOND);
            case DIAMOND -> {
                return;
            }
            default -> user.setLevel(Level.BRONZE);
        };
    }


    public void updateUserPassword(@NonNull User user, @NonNull String newPassword) {
        String newEncryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newEncryptedPassword);
        repo.save(user);
    }

    public User getUserById(@NonNull Integer userId) {
        return repo.getReferenceById(userId);
    }


    public int countUserRegistrations(LocalDateTime from, LocalDateTime to) {
       List<User> listOfRegisteredUsersPastSevenDays = repo.findByCreatedAtBetween(from, to);

       return listOfRegisteredUsersPastSevenDays.size();
    }

    public int countAllRegisteredUsers() {
        List<User> allUsers = repo.findAll();

        return allUsers.size();
    }

}
