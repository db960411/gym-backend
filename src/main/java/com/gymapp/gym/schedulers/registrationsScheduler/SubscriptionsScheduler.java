package com.gymapp.gym.schedulers.registrationsScheduler;

import com.gymapp.gym.DashboardGraphSummary.SubscriptionSummary.SubscriptionSummary;
import com.gymapp.gym.DashboardGraphSummary.SubscriptionSummary.SubscriptionSummaryRepository;
import com.gymapp.gym.subcriptionEvents.SubscriptionEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class SubscriptionsScheduler {
    @Autowired
    private SubscriptionEventService subscriptionEventService;
    @Autowired
    private SubscriptionSummaryRepository repository;

    @Scheduled(cron = "0 55 23 * * *")
    public SubscriptionSummary countRegisteredSubscriptionsPerWeek() {
        log.info("Running weekly subscriptions scheduler...");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        int amountOfNewRegisteredSubscriptionsPastSevenDays = subscriptionEventService.countWeeklySubscriptions(sevenDaysAgo, now);

        SubscriptionSummary subscriptionSummary = new SubscriptionSummary();

        SubscriptionSummary latestSubscriptionSummary = repository.findFirstByOrderByCreatedAtDesc();

        if (latestSubscriptionSummary == null) {
            subscriptionSummary.setWeek(1);
        } else {
            subscriptionSummary.setWeek(latestSubscriptionSummary.getWeek() + 1);
        }

        subscriptionSummary.setName(String.valueOf(subscriptionSummary.getWeek()));
        subscriptionSummary.setValue(amountOfNewRegisteredSubscriptionsPastSevenDays);
        subscriptionSummary.setCreatedAt(now);

        repository.save(subscriptionSummary);

        log.info("weekly subscriptions scheduler finished!");

        return subscriptionSummary;
    }
}
