package com.gymapp.gym.DashboardGraphSummary.SubscriptionSummary;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SubscriptionSummaryResponse {
    private UUID id;
    private Date createdAt;
    private String name;
    private int week;
    private int amount;
}
