package com.gymapp.gym.plans;

import com.gymapp.gym.subscription.SubscriptionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plans")
public class Plans {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String description;
    private String category;
    @Enumerated(EnumType.STRING)
    private SubscriptionType subscription_type;

}
