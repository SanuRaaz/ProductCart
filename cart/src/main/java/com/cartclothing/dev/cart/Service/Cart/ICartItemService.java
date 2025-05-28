package com.cartclothing.dev.cart.Service.Cart;

import com.cartclothing.dev.cart.Modal.Cart;
import com.cartclothing.dev.cart.Modal.CartItem;

import java.math.BigDecimal;

public interface ICartItemService {

    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
     void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);
}
