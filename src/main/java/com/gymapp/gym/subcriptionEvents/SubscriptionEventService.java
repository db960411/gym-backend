package com.gymapp.gym.subcriptionEvents;

import com.gymapp.gym.subscription.Subscription;
import com.gymapp.gym.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionEventService {
    @Autowired
    private SubscriptionEventRepository repository;

    public void addSubscriptionEvent(User user) {
        SubscriptionEvent subscriptionEvent = new SubscriptionEvent();

        subscriptionEvent.setUser(user);
        subscriptionEvent.setCreatedAt(LocalDateTime.now());

        repository.save(subscriptionEvent);
    }

    public int countWeeklySubscriptions(LocalDateTime from, LocalDateTime to){
        List<SubscriptionEvent> listOfRegisteredUsersPastSevenDays = repository.findByCreatedAtBetween(from, to);
        return listOfRegisteredUsersPastSevenDays.size();
    }
}
