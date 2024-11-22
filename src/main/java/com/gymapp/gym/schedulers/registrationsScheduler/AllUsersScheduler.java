package com.gymapp.gym.schedulers.registrationsScheduler;

import com.gymapp.gym.DashboardGraphSummary.SubscriptionSummary.SubscriptionSummary;
import com.gymapp.gym.DashboardGraphSummary.UserRegistration.AllUsersSummary;
import com.gymapp.gym.DashboardGraphSummary.UserRegistration.AllUsersSummaryRepository;
import com.gymapp.gym.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AllUsersScheduler {
    @Autowired
    private UserService userService;
    @Autowired
    private AllUsersSummaryRepository allUsersSummaryRepository;

    @Scheduled(cron = "0 58 23 * * *")
    public AllUsersSummary countRegisteredSubscriptionsPerWeek() {
        log.info("Running weekly count of users scheduler...");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        int amountOfUsersPastSevenDays = userService.countAllRegisteredUsers();

        AllUsersSummary allUsersSummary = new AllUsersSummary();

        AllUsersSummary latestAllUserSummary = allUsersSummaryRepository.findFirstByOrderByCreatedAtDesc();

        if (latestAllUserSummary == null) {
            allUsersSummary.setWeek(1);
        } else {
            allUsersSummary.setWeek(latestAllUserSummary.getWeek() + 1);
        }

        allUsersSummary.setAmount(amountOfUsersPastSevenDays);
        allUsersSummary.setCreatedAt(now);

        allUsersSummaryRepository.save(allUsersSummary);

        log.info("weekly subscriptions scheduler finished!");

        return allUsersSummary;
    }
}
