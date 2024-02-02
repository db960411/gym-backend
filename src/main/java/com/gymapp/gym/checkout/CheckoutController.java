package com.gymapp.gym.checkout;

import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CheckoutController {

    private final CheckoutService service;

    @GetMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession() {
        return ResponseEntity.ok(service.getStripeKey());
    }

    @PostMapping("/createPaymentIntent")
    public ResponseEntity<CheckoutResponse> getPaymentIntent(@RequestBody String stripeToken, HttpServletRequest httpServletRequest) throws StripeException, IllegalAccessException {
        return ResponseEntity.ok(service.createPaymentIntent(stripeToken, httpServletRequest));
    }
    @GetMapping("/updateUserToSubscribedMember")
    public ResponseEntity<CheckoutResponse> updateUserToSubscribedMember(HttpServletRequest httpServletRequest) throws IllegalAccessException {
        return ResponseEntity.ok(service.updateUserToSubscribedMember(httpServletRequest));
    }

}
