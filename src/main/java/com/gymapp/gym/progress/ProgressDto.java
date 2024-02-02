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
  private double sets;
  private double reps;
  private double weight;
  private double distance;
  private double time;
}
