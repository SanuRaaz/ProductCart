package com.cartclothing.dev.cart.Exception;

public class CategoryNotFoundException extends RuntimeException
{
    public CategoryNotFoundException(String  message) {
        super(message);
    }
}
