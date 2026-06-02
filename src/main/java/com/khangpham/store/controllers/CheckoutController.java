package com.khangpham.store.controllers;

import com.khangpham.store.dtos.CheckoutRequest;
import com.khangpham.store.dtos.ErrorDto;
import com.khangpham.store.entities.OrderStatus;
import com.khangpham.store.exceptions.CartIsEmptyException;
import com.khangpham.store.exceptions.CartNotFoundException;
import com.khangpham.store.exceptions.PaymentErrorException;
import com.khangpham.store.repositories.OrderRepository;
import com.khangpham.store.services.CheckoutService;
import com.khangpham.store.services.WebhookRequest;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {
        var checkoutResponse = checkoutService.checkout(request.getCartId());
        return ResponseEntity.ok(checkoutResponse);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(
            @RequestHeader() Map<String, String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler({CartNotFoundException.class, CartIsEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(PaymentErrorException.class)
    public ResponseEntity<ErrorDto> handleExceptionPaymentError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorDto("Checkout processing error")
        );
    }
}
