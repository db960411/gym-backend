package com.gymapp.gym.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {
    private Page<News> newsList;
    private String errorMessage;
    private String title;
    private String body;
    private String category;
    private String author;
    private Date createdAt;
    private String imageUrl;
    private long totalNews;
}
