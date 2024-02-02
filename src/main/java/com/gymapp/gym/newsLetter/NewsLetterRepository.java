package com.gymapp.gym.newsLetter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLetterRepository extends JpaRepository<NewsLetter, Integer> {

     NewsLetter findByUserId(Integer userId);

     Boolean existsByUserId(Integer userId);

}
