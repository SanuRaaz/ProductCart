package com.cartclothing.dev.cart.Repository;

import com.cartclothing.dev.cart.Modal.Cart;
import com.cartclothing.dev.cart.Modal.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
   Set<CartItem> getItemsById(Long id);
}
