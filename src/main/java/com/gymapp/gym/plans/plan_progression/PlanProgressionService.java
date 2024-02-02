package com.gymapp.gym.plans.plan_progression;

import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlanProgressionService {
    @Autowired
    private PlanProgressionRepository repository;
    @Autowired
    private UserService userService;


    public PlanProgression getPlanProgressionByUserId(int userId) {
        return repository.getByUserId(userId);
    };

     public void deletePlanProgressionById(int userId) {
         repository.deleteById(userId);
     }

     public ResponseEntity<PlanProgressionDto> updatePlanProgressionDay(HttpServletRequest request) {
         final String email = request.getHeader("Email");
         User user = userService.getUserByEmail(email);

         if (user == null) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
         }

         PlanProgression planProgression = repository.getByUserId(user.getId());

         if (planProgression == null) {
             return ResponseEntity.badRequest().build();
         }

         PlanProgressionDto planProgressionDto = new PlanProgressionDto();

         if (planProgression.getDay() <= 30) {
             planProgression.setDay(planProgression.getDay() + 1);
             repository.save(planProgression);
         } else {
             planProgressionDto.setDay(planProgression.getDay());
             return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(planProgressionDto);
         }

         planProgressionDto.setDay(planProgression.getDay());
         planProgressionDto.setPlan(planProgression.getPlan());

         return ResponseEntity.ok().body(planProgressionDto);
     }

    public ResponseEntity<PlanProgressionDto> cancelPlanProgressionByUser(HttpServletRequest request) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PlanProgression planProgression = repository.getByUserId(user.getId());

        if (planProgression == null) {
            return ResponseEntity.badRequest().build();
        }

        repository.delete(planProgression);

        return ResponseEntity.ok().build();
    }

}
