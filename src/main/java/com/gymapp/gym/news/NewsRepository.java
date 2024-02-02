package com.gymapp.gym.news;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query("SELECT n FROM News n WHERE (:category IS NULL OR n.category ILIKE :category)")
    Page<News> findAllByCategory(String category, Pageable pageable);



    News findById(String id);
}

