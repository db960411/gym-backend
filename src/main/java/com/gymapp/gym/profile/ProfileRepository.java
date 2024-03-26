package com.gymapp.gym.profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer>{
    Profile findByDisplayName(String displayName);
    Profile findByUserEmail(String email);
    boolean existsByUserEmail(String displayName);
    Profile getByUserId(Integer id);

}
