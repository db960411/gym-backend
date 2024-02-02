package com.gymapp.gym.settings;

import com.gymapp.gym.plans.Plans;
import com.gymapp.gym.plans.plan_progression.PlanProgressionDto;
import com.gymapp.gym.subscription.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDto {
    private String email;
    private SubscriptionType subscriptionType;
    private boolean verifiedEmail;
    private String language;
    private boolean receiveEmails;
    private PlanProgressionDto planProgressionDto;
    private Plans plan;
    private boolean allowNotifications;
}
