package com.gymapp.gym.DashboardGraphSummary.UserRegistration;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
public class UserRegistrationSummaryResponse {
    private int week;
    private int amount;
    private Date createdAt;

    public UserRegistrationSummaryResponse(int week, int amount, Date createdAt) {
        this.week = week;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}
