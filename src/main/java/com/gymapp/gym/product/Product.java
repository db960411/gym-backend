package com.gymapp.gym.product;

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
@Table
public class Product {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String price;
    private String category;
    private boolean active;
    private boolean recurring_item;

}
