package com.gymapp.gym.plans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlansResponse {
    private String errorMessage;
    private String successMessage;
    private int day;
    private Plans plan;

    public PlansResponse(String errorMessage, String successMessage, int day, Plans plan) {
        this.errorMessage = errorMessage;
        this.successMessage = successMessage;
        this.day = day;
        this.plan = plan;
    }
}
