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
    private boolean completed;
    private boolean active;

    public PlanProgressionDto(PlanProgression planProgression) {
        this.day = planProgression.getDay();
        this.plan = planProgression.getPlan();
        this.completed = planProgression.isCompleted();
        this.active = planProgression.isActive();
    }
}
