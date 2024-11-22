package com.gymapp.gym.schedulers.registrationsScheduler;


import com.gymapp.gym.DashboardGraphSummary.UserRegistration.UserRegistrationSummary;
import com.gymapp.gym.DashboardGraphSummary.UserRegistration.UserRegistrationSummaryRepository;
import com.gymapp.gym.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class UserRegistrationScheduler {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRegistrationSummaryRepository repository;

    @Scheduled(cron = "0 50 23 * * *")
    public UserRegistrationSummary countRegisteredUsersPerWeek() {
        log.info("Running userRegistrationSummary scheduler...");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        int amountOfNewRegisteredUsersPastSevenDays = userService.countUserRegistrations(sevenDaysAgo, now);

        UserRegistrationSummary userRegistrationSummary = new UserRegistrationSummary();

        UserRegistrationSummary latestUserRegistrationSummary = repository.findFirstByOrderByCreatedAtDesc();

        if (latestUserRegistrationSummary == null) {
            userRegistrationSummary.setWeek(1);
        } else {
            userRegistrationSummary.setWeek(latestUserRegistrationSummary.getWeek() + 1);
        }

        userRegistrationSummary.setAmount(amountOfNewRegisteredUsersPastSevenDays);
        userRegistrationSummary.setCreatedAt(now);

        repository.save(userRegistrationSummary);

        log.info("UserRegistrationSummary scheduler finished!");

        return userRegistrationSummary;
    }
}
