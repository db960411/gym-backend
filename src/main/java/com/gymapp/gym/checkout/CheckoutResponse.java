package com.gymapp.gym.checkout;

import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {
    private PaymentIntent paymentIntent;
    private String errorMessage;
    private String successMessage;
    private String clientSecret;
}
