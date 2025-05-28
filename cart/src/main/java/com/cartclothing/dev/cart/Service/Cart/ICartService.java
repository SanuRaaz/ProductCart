package com.cartclothing.dev.cart.Service.Cart;

import com.cartclothing.dev.cart.Modal.Cart;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long cartId);
    void clearCart(Long Id);
    BigDecimal getTotalPrice(Long id);
    Long initializeNewCart();
}
