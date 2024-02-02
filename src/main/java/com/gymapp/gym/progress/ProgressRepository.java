package com.gymapp.gym.progress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProgressRepository extends JpaRepository<Progress, UUID> {

    List<Progress> findByProfileId(Integer id);

    Progress getById(UUID id);

    List<Progress> getAllByProfileId(Integer id);

}
