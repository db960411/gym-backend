package com.gymapp.gym.plans.plan_progression;

import com.gymapp.gym.plans.Plans;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanProgressionDto {
    private int day;
    private Plans plan;

    public PlanProgressionDto(PlanProgression planProgression, int day, Plans plan) {
        this.day = planProgression.getDay();
        this.plan = planProgression.getPlan();
    }
}
