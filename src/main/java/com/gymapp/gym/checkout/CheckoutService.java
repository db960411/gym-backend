package com.gymapp.gym.checkout;

import com.gymapp.gym.profile.ProfileService;
import com.gymapp.gym.subscription.Subscription;
import com.gymapp.gym.subscription.SubscriptionService;
import com.gymapp.gym.subscription.SubscriptionType;
import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private static final String EMAIL_HEADER = "Email";
    private static final String DISPLAY_NAME_NULL_ERROR = "Display name is null";
    private static final String USER_NULL_ERROR = "User is null";
    private static final String PAYMENT_INITIALIZED_MESSAGE = "Payment initialized";
    @Autowired
    private final SubscriptionService subscriptionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;

    @Value("${spring.stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createCheckoutSession() throws StripeException {
        // Define the line item
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("sek")
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Standard Membership")
                                                .build()
                                )
                                .setUnitAmount(15000L)  // Amount in cents
                                .build()
                )
                .setQuantity(1L)
                .build();

        // Define the session creation parameters
        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/checkout")
                .build();

        // Create the session
        Session session = Session.create(params);
        return session.getId();
    }

    public CheckoutResponse updateUserToSubscribedMember(@NotNull HttpServletRequest request) throws IllegalAccessException {
        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new IllegalAccessException("No user found by this request");
        }

        Subscription subscription = subscriptionService.getByUserId(user.getId());

        if (subscription.getSubscriptionType().equals(SubscriptionType.BASIC)) {
            subscriptionService.subscribeToStandard(email);
        }

        return CheckoutResponse.builder().successMessage("Updated user subscription!").build();
    }


}
