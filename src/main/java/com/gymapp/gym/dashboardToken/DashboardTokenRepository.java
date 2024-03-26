package com.gymapp.gym.dashboardToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardTokenRepository extends JpaRepository<DashboardToken, Integer> {

    DashboardToken getByToken(int tokenId);
}
