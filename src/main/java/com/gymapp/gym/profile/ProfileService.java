package com.gymapp.gym.profile;

import com.gymapp.gym.progress.ProgressService;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserRepository;
import com.gymapp.gym.analytics.UserAnalytics.UserAnalytics;
import com.gymapp.gym.analytics.UserAnalytics.UserAnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.gymapp.gym.progress.ProgressService.calculatePercentageIncrease;

@Service
@RequiredArgsConstructor
public class ProfileService {
    @Autowired
    private final ProfileRepository repository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserAnalyticsService userAnalyticsService;

    public ProfileResponse getProfileByDisplayName(@NonNull String displayName) {
        Profile profile = repository.findByDisplayName(displayName);

        if (profile == null) {
            return new ProfileResponse("Profile not found by name: " + displayName);
        }

        return new ProfileResponse(profile);
    }

    public String getProfileEmailAdress(@NonNull User user) {
        User userByEmail = userRepository.getUserByEmail(user.getEmail());

        if (userByEmail == null) {
            return null;
        }

        return userByEmail.getEmail();
    }

    public Profile getProfile(@NonNull String email) {
        return repository.findByUserEmail(email);
    }

    public ProfileResponse profileStatus(@NonNull HttpServletRequest request) {
        final String email = request.getHeader("Email");
        Profile profile = repository.findByUserEmail(email);

        if (profile == null) {
            return new ProfileResponse("Profile not created");
        }

        return new ProfileResponse(toProfileDto(profile));
    }

    public ProfileResponse createProfile(@NonNull HttpServletRequest request, ProfileDto profileDto) {
        final String email = request.getHeader("Email");

        if (email == null) {
            return new ProfileResponse("Email not found in the request headers.");
        }

        boolean profileExists = repository.existsByUserEmail(email);
        if (profileExists) {
            return new ProfileResponse("Profile already created for " + email);
        }

        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if(optionalUser.isEmpty()) {
            return new ProfileResponse("User not found for " + email);
        }

        User user = optionalUser.get();
        Profile profile = Profile.builder().user(user).displayName(profileDto.getDisplayName()).height(profileDto.getHeight()).weight(profileDto.getWeight()).language(profileDto.getLanguage()).nationality(profileDto.getNationality())
                .gender(profileDto.getGender()).dateOfBirth(profileDto.getDateOfBirth()).fitnessGoals(profileDto.getFitnessGoals()).build();

        repository.save(profile);

        UserAnalytics userAnalytics = new UserAnalytics();
        userAnalytics.setUser(user);
        userAnalytics.setInitialWeight(profile.getWeight());
        userAnalytics.setInitialBodyFatPercentage(0);
        userAnalytics.setInitialBMI(userAnalyticsService.calculateBMI(profile.getWeight(),profile.getHeight()));
        userAnalytics.setCurrentBMI(0);
        userAnalytics.setWorkOutDaysDone(0);
        userAnalytics.setCurrentLongestWorkout(0);
        userAnalytics.setInitialLongestWorkout(0);
        userAnalytics.setInitialSlowWaveSleep(0);
        userAnalytics.setCurrentSlowWaveSleep(0);

        userAnalyticsService.createUserAnalyticsForUser(userAnalytics);

        return new ProfileResponse(profileDto);
    }

    public void updateProfileLanguageForUser(@NonNull User user, @NonNull String language) {
                Profile profile = repository.getByUserId(user.getId());
                if (profile != null) {
                    profile.setLanguage(language);
                    repository.save(profile);
                }
        new ProfileResponse(profile);
    }

    public ProfileDto updateProfile(HttpServletRequest request, ProfileDto profileData) {
        final String email = request.getHeader("Email");
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User null when trying to update profile");
        }

        Profile profile = getProfile(email);

        if (profile == null) {
            throw new RuntimeException("Profile null when trying to update profile");
        }

        if (profileData.getLanguage() != null) {
            profile.setLanguage(profileData.getLanguage());
        }
        if (profileData.getHeight() != 0) {
            profile.setHeight(profileData.getHeight());
        }
        if (profileData.getWeight() != 0) {
            profile.setWeight(profileData.getWeight());
        }
        if (profileData.getDateOfBirth() != null) {
            profile.setDateOfBirth(profileData.getDateOfBirth());
        }
        if (profileData.getDateOfBirth() != null) {
            profile.setDateOfBirth(profileData.getDateOfBirth());
        }
        if (profileData.getFitnessGoals() != null) {
            profile.setFitnessGoals(profileData.getFitnessGoals());
        }
        if (profileData.getDisplayName() != null) {
            profile.setDisplayName(profileData.getDisplayName());
        }
        if (profileData.getNationality() != null) {
            profile.setNationality(profileData.getNationality());
        }

        repository.save(profile);

        UserAnalytics userAnalytics = userAnalyticsService.getByUser(user);
        userAnalytics.setModifiedAt(Date.from(Instant.now()));
        userAnalytics.setCurrentWeight(profileData.getWeight());
        userAnalytics.setWeightPercentageIncrease(calculatePercentageIncrease(userAnalytics.getInitialWeight(), profileData.getWeight()));
        userAnalytics.setCurrentBMI(userAnalyticsService.calculateBMI(profile.getWeight(), profile.getHeight()));

        userAnalyticsService.updatedUserAnalyticsForUser(userAnalytics);

        return toProfileDto(profile);
    }

    public Profile getByUserId(Integer id) {
       return repository.getByUserId(id);
    }

    public ProfileDto toProfileDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setDisplayName(profile.getDisplayName());
        profileDto.setHeight(profile.getHeight());
        profileDto.setWeight(profile.getWeight());
        profileDto.setGender(profile.getGender());
        profileDto.setLanguage(profile.getLanguage());
        profileDto.setNationality(profile.getNationality());
        profileDto.setDateOfBirth(profile.getDateOfBirth());
        profileDto.setFitnessGoals(profile.getFitnessGoals());

        return profileDto;
    }

}
