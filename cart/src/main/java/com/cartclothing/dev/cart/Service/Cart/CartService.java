package com.cartclothing.dev.cart.Service.Cart;

import com.cartclothing.dev.cart.Exception.ResourceNotFoundException;
import com.cartclothing.dev.cart.Modal.Cart;
import com.cartclothing.dev.cart.Modal.CartItem;
import com.cartclothing.dev.cart.Repository.CartItemRepository;
import com.cartclothing.dev.cart.Repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
//@RequiredArgsConstructor
public class CartService implements ICartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
     CartItemRepository cartItemRepository;

    private final AtomicLong cartIdGenerator = new AtomicLong (0);

    @Override
    public Cart getCart(Long cartId) {
        Cart cart =  cartRepository.findById (cartId)
                .orElseThrow(() -> new ResourceNotFoundException ("Cart is Not Present"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);

    }

    @Override
    @Transactional
    public void clearCart(Long id) {
         Cart cart = getCart (id);
         cartItemRepository.deleteAllByCartId(id);
        //cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id)
    {
      Cart cart = getCart(id);
       return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart()
    {
        Cart newCart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet ();
        System.out.println(newCartId + "is present");
        newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();
    }
}
