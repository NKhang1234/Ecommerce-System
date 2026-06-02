package com.khangpham.store.services;

import com.khangpham.store.dtos.CheckoutResponse;
import com.khangpham.store.entities.Order;
import com.khangpham.store.entities.OrderStatus;
import com.khangpham.store.exceptions.CartIsEmptyException;
import com.khangpham.store.exceptions.CartNotFoundException;
import com.khangpham.store.exceptions.PaymentErrorException;
import com.khangpham.store.repositories.CartRepository;
import com.khangpham.store.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponse checkout(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();

        }

        if (cart.isEmpty()) {
            throw new CartIsEmptyException();
        }

        var order = Order.createOrder(cart, authService.getCurrentUser());

        orderRepository.save(order);

        try {
            // Create checkout session
            var session = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());

        } catch(PaymentErrorException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        try {
            var event = Webhook.constructEvent(request.getPayload(), request.getHeaders(), webhookSecretKey);
            System.out.println(event.getType());

            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    // Update order status (PAID)
                    var paymentIntent = (PaymentIntent) stripeObject;
                    if (paymentIntent != null) {
                        var orderId = paymentIntent.getMetadata().get("orderId");
                        var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }
                }

                case "payment_intent.failed" -> {
                    // Update order status (FAILED)
                }
            }

            return ResponseEntity.ok().build();


        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
