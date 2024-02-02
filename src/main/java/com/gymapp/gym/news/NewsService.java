package com.gymapp.gym.news;

import com.gymapp.gym.notes.Notes;
import com.gymapp.gym.notes.NotesDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository repository;

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
}
