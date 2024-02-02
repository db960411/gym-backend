package com.gymapp.gym.checkout;


import lombok.Data;

@Data
public class ChargeRequest {
    private String description;
    private Long amount;
    private String currency;
    private String stripeEmail;
    private String stripeToken;
    private String customerDisplayName;
}
