package com.gymapp.gym.checkoutToken;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutTokenController {
    @Autowired
    private CheckoutTokenService checkoutTokenService;

    @PostMapping("verify-token")
    public CheckoutTokenDto verifyCheckoutTokenById(HttpServletRequest request, @RequestBody int tokenId) {
        return checkoutTokenService.verifyCheckoutByToken(request, tokenId);
    }


}
