package com.cartclothing.dev.cart.Exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String  message) {
        super(message);
    }
}
