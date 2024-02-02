package com.gymapp.gym.plans;

import com.gymapp.gym.exceptions.UserNotFoundException;
import com.gymapp.gym.plans.plan_progression.PlanProgression;
import com.gymapp.gym.plans.plan_progression.PlanProgressionRepository;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlansService {
    @Autowired
    private PlansRepository repository;
    @Autowired
    private PlanProgressionRepository planProgressionRepository;
    @Autowired
    private UserService userService;

    public PlansResponse checkPlanStatusForUser(HttpServletRequest request) throws UserNotFoundException {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("No user found for this email: " + email);
        }

        PlanProgression userPlanProgression = planProgressionRepository.getByUserId(user.getId());

        if (userPlanProgression != null) {
            return new PlansResponse.PlansResponseBuilder().day(userPlanProgression.getDay()).plan(userPlanProgression.getPlan()).successMessage(userPlanProgression.getPlan().getName()).build();
        }

        return new PlansResponse.PlansResponseBuilder().errorMessage("Not on a plan").build();
    }

    public PlansResponse assignPlanToUser(HttpServletRequest request, @NonNull String selectedPlan) throws UserNotFoundException {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("No user found for this email: " + email);
        }

        Plans planBySelectedPlan = repository.getByName(selectedPlan);

        if (planBySelectedPlan == null) {
            throw new NullPointerException("Selected plan doesn't exist: " + selectedPlan);
        }

        PlanProgression userPlanProgression = planProgressionRepository.getByUserId(user.getId());

        if (userPlanProgression != null && userPlanProgression.getPlan().getName().equals(selectedPlan)) {
          return new PlansResponse.PlansResponseBuilder().errorMessage("User already assigned to a plan " + userPlanProgression.getPlan().getName()).build();
        }

        if (userPlanProgression == null) {
            PlanProgression planProgression = PlanProgression.builder().user(user).plan(planBySelectedPlan).day(1).build();
            planProgressionRepository.save(planProgression);
        }

        return new PlansResponse.PlansResponseBuilder().successMessage("User was assigned to plan " + selectedPlan).build();
    }
}
