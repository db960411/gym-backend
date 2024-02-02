package com.gymapp.gym.notifications;

import com.gymapp.gym.social.Social;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {

    List<Notifications> findAllBySocialId(int socialId, Pageable pageable);

}
