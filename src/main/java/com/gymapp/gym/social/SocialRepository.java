package com.gymapp.gym.social;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialRepository extends JpaRepository<Social, Integer> {

    Social getByUserId(Integer id);

    Social getById(Integer id);

    Optional<Social> findById(Integer id);
}
