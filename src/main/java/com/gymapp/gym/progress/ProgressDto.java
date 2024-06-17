package com.gymapp.gym.progress;

import com.gymapp.gym.exerciseType.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressDto {
  private UUID id;
  private ExerciseType exerciseType;
  private Double sets;
  private Double reps;
  private Double weight;
  private Double distance;
  private Double time;
  private Double Steps;
  private Double heartRate;
}
