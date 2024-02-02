package com.gymapp.gym.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Integer> {

    Settings getByUserEmail(String email);

    Settings getByUserId(Integer id);

}
