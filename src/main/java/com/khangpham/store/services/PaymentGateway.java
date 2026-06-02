package com.khangpham.store.services;

import com.khangpham.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
