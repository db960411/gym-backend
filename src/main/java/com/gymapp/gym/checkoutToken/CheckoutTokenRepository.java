package com.gymapp.gym.checkoutToken;

import com.gymapp.gym.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckoutTokenRepository extends JpaRepository<CheckoutToken, Integer> {

    CheckoutToken getByToken(Integer tokenId);

    CheckoutToken findFirstByUserOrderByCreatedAtDesc(User user);

}
