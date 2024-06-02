package com.gymapp.gym.progress;


import com.gymapp.gym.exerciseType.ExerciseType;
import com.gymapp.gym.profile.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "progress")
public class Progress {
    @GeneratedValue
    @Id
    UUID id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "exercise_type_id")
    private ExerciseType exerciseType;
    private double sets;
    private double reps;
    private double weight;
    private double distance;
    private double time;
    private Double steps;
    private Double heartRate;

}
