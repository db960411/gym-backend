package com.gymapp.gym.plans.plan_progression;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanProgressionRepository extends JpaRepository<PlanProgression, Integer> {

    PlanProgression getByUserId(Integer id);

}
