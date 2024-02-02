package com.gymapp.gym.notes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotesRepository extends JpaRepository<Notes, UUID> {
    @Query("SELECT n FROM Notes n JOIN n.user u WHERE u.id = :userId AND (:category IS NULL OR n.category = :category)")
    Page<Notes> findAllByUserIdAndCategory(int userId, String category, Pageable pageable);

}
