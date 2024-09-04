package com.gymapp.gym.news;

import com.gymapp.gym.notes.Notes;
import com.gymapp.gym.notes.NotesDto;
import com.gymapp.gym.user.Role;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository repository;
    @Autowired
    private UserService userService;

    public NewsResponse getNews(Pageable pageable, String category) {
        if (category != null && category.isEmpty()) {
            category = null;
        }

        Page<News> newsList = repository.findAllByCategory(category, pageable);
        if (newsList.isEmpty()) {
            return NewsResponse.builder().errorMessage("No news available").build();
        }

        long totalNotes = newsList.getTotalElements();

        return NewsResponse.builder().newsList(newsList).totalNews(totalNotes).build();
    }

    public NewsResponse getSpecificNews(String blogId) {
        News news = repository.findById(blogId);

        if (news == null) {
            throw new RuntimeException("No news found for this id");
        }

        return NewsResponse.builder().title(news.getTitle()).body(news.getBody()).createdAt(news.getCreatedAt()).author(news.getAuthor()).category(news.getCategory()).imageUrl(news.getImageUrl()).build();
    }

    public NewsResponse createBlogPost(HttpServletRequest request, NewsResponse newsResponse) {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found when trying to create blog post");
        }

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User not validated to post blog post.");
        }

        News news = new News();
        news.setCreatedAt(Date.from(Instant.now()));
        news.setAuthor("Admin");
        news.setTitle(newsResponse.getTitle());
        news.setBody(newsResponse.getBody());
        news.setCategory(newsResponse.getCategory());
        if (newsResponse.getImageUrl() != null) {
            news.setImageUrl(newsResponse.getImageUrl());
        } else {
            news.setImageUrl("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8Z3ltfGVufDB8fDB8fHww");
        }

        repository.save(news);

        return NewsResponse.builder().build();
    }
}
