package com.gymapp.gym.checkoutToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutTokenRepository extends JpaRepository<CheckoutToken, Integer> {

    CheckoutToken getByToken(Integer tokenId);
}
