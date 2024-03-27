package com.gymapp.gym.settings;


import com.gymapp.gym.checkoutToken.CheckoutTokenService;
import com.gymapp.gym.email.EmailService;
import com.gymapp.gym.plans.plan_progression.PlanProgression;
import com.gymapp.gym.plans.plan_progression.PlanProgressionDto;
import com.gymapp.gym.plans.plan_progression.PlanProgressionService;
import com.gymapp.gym.subscription.Subscription;
import com.gymapp.gym.subscription.SubscriptionService;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SettingsService {

    @Autowired
    private final UserService userService;
    @Autowired
    private final SettingsRepository settingsRepository;
    @Autowired
    private final SubscriptionService subscriptionService;
    @Autowired
    private final PlanProgressionService planProgressionService;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final CheckoutTokenService checkoutTokenService;

    @Value("${verification.url}")
    private String verificationUrl;

    public SettingsResponse updateEmail(HttpServletRequest request, @NonNull String newEmail) {
        final String email = request.getHeader("email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return new SettingsResponse.SettingsResponseBuilder().errorMessage("User doesn't exist").build();
        }

        String jwtToken = userService.updateUserEmail(user, newEmail);

        if (jwtToken != null) {
            return new SettingsResponse.SettingsResponseBuilder().jwtToken(jwtToken).email(newEmail).successMessage("Email updated.").build();
        } else {
            return new SettingsResponse.SettingsResponseBuilder().errorMessage("Email already taken").build();
        }
    }

    public SettingsResponse updatePassword(HttpServletRequest request, @NonNull String newPassword) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return new SettingsResponse("No user found");
        }

        userService.updateUserPassword(user, newPassword);

        return new SettingsResponse.SettingsResponseBuilder().successMessage("Updated user password successfully").build();
    }

    public SettingsResponse updateLanguage(HttpServletRequest request, @NonNull String language) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return new SettingsResponse("No user found");
        }

        Settings settings = settingsRepository.getByUserId(user.getId());

        if (settings == null) {
            return new SettingsResponse("No settings found for this user...");
        }

        settings.setLanguage(language);
        settingsRepository.save(settings);

        return new SettingsResponse.SettingsResponseBuilder().userLanguage(language).successMessage("Updated user language successfully").build();
    }

    public SettingsResponse cancelUserPlanprogression(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return new SettingsResponse("No user found");
        }

        PlanProgression planProgression = planProgressionService.getPlanProgressionByUserId(user.getId());

        if (planProgression != null) {
          planProgressionService.deletePlanProgressionById(planProgression.getId());
        }

        return new SettingsResponse.SettingsResponseBuilder().successMessage("Removed user from plan").build();
    }

    public SettingsResponse receiveEmailsByUser(HttpServletRequest request, boolean selectedValue) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return new SettingsResponse("No user found");
        }

        Settings settings = settingsRepository.getByUserEmail(email);

        if (settings == null) {
            return new SettingsResponse.SettingsResponseBuilder().errorMessage("User has no settings associated").build();
        }

        if (selectedValue == settings.isReceiveEmails()) {
            return new SettingsResponse.SettingsResponseBuilder().errorMessage("User has selected already existing value").build();
        }

        settings.setReceiveEmails(selectedValue);
        settingsRepository.save(settings);

        return new SettingsResponse.SettingsResponseBuilder().successMessage("Updated email preferences").build();
    }

    public SettingsResponse allowNotificationsByUser(HttpServletRequest request, boolean selectedValue) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return new SettingsResponse("No user found");
        }

        Settings settings = settingsRepository.getByUserEmail(email);

        if (settings == null) {
            return new SettingsResponse.SettingsResponseBuilder().errorMessage("User has no settings associated").build();
        }

        if (selectedValue == settings.isAllowNotifications()) {
            return new SettingsResponse.SettingsResponseBuilder().errorMessage("User has selected already existing value").build();
        }

        settings.setAllowNotifications(selectedValue);
        settingsRepository.save(settings);

        return new SettingsResponse.SettingsResponseBuilder().successMessage("Updated notifications preferences").build();
    }

    public SettingsResponse sendVerificationEmailForUser(HttpServletRequest request) throws IllegalAccessException {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalAccessException("No user found");
        }

        Settings settings = settingsRepository.getByUserId(user.getId());

        if (settings == null) {
           throw new IllegalAccessException("This user has no settings associated");
        }

        final int verificationCode = checkoutTokenService.createCheckoutTokenForUser(user).getToken();
        final String mailSubject = "Verify your email";
        final String mailText = "Click the link below to verify your email please\n" + verificationUrl + verificationCode;

        if (verificationCode > 100) {
            emailService.addToEmailQueue(user.getEmail(), mailSubject, mailText);
        }

        return new SettingsResponse.SettingsResponseBuilder().successMessage("Email verification email sent successfully!").build();
    }

    public SettingsDto getOrCreateSettingsByUser(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        Settings settings = settingsRepository.getByUserEmail(email);

        if (settings == null) {
            User user = userService.getUserByEmail(email);
            Subscription subscription = subscriptionService.getByUserId(user.getId());
            settings = Settings.builder().receiveEmails(false).allowNotifications(true).user(user).subscription(subscription).language("English").build();
            settingsRepository.save(settings);
        }

        return toSettingsDto(settings);
    }

    public void createSettingsForUser(User user) {
        Settings settings = settingsRepository.getByUserEmail(user.getEmail());

        if (settings == null) {
            Subscription subscription = subscriptionService.getByUserId(user.getId());
            settings = Settings.builder().receiveEmails(false).allowNotifications(true).subscription(subscription).user(user).language("English").build();
            settingsRepository.save(settings);
        }
    }

    public Settings getSettingsByUser(User user) {
        return settingsRepository.getByUserId(user.getId());
    }

    public SettingsDto toSettingsDto(Settings settings) {
        if (settings == null) {
            return null;
        }
        SettingsDto settingsDto = new SettingsDto();
        settingsDto.setEmail(settings.getUser().getEmail());
        settingsDto.setSubscriptionType(settings.subscription.getSubscriptionType());
        settingsDto.setVerifiedEmail(settings.subscription.isVerified_email());
        settingsDto.setReceiveEmails(settings.isReceiveEmails());
        settingsDto.setAllowNotifications(settings.isAllowNotifications());
        settingsDto.setLanguage(settings.getLanguage());

        PlanProgression planProgression = planProgressionService.getPlanProgressionByUserId(settings.getUser().getId());
        if (planProgression != null) {
            PlanProgressionDto planProgressionDto = new PlanProgressionDto();
            planProgressionDto.setDay(planProgression.getDay());
            planProgressionDto.setPlan(planProgression.getPlan());
            settingsDto.setPlanProgressionDto(planProgressionDto);
        }

        return settingsDto;
    }

}


