package com.gymapp.gym.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 2048)
    private String body;
    private String title;
    private String category;
    private String author;
    private String imageUrl;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

}
