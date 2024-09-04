package com.gymapp.gym.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getUserByEmail(String email);

    Optional<User> findUserByEmail(String email);

    List<User> findByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);

}
