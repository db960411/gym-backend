package com.gymapp.gym.exerciseType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "exercise_type")
public class ExerciseType {
    @Id
    @GeneratedValue
    UUID id;

    private String name;
}
