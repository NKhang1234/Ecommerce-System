package com.khangpham.store.exceptions;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException() {
        super("Cart is not found");
    }
}
