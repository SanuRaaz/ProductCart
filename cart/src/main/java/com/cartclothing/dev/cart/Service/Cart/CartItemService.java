package com.cartclothing.dev.cart.Service.Cart;


import com.cartclothing.dev.cart.Exception.ResourceNotFoundException;
import com.cartclothing.dev.cart.Modal.Cart;
import com.cartclothing.dev.cart.Modal.CartItem;
import com.cartclothing.dev.cart.Modal.Product;
import com.cartclothing.dev.cart.Repository.CartItemRepository;
import com.cartclothing.dev.cart.Repository.CartRepository;
import com.cartclothing.dev.cart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements  ICartItemService {

    private  final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. get the cart
        //2. get the product
        //3. check if the product already exist /in the cart
        //4. if yes, update quantity (increase the quantity with request quantity)
        //5. if no, initates the new cart item entry in the cart

        Cart cart = cartService.getCart (cartId);
        Product product = productRepository.getProductById (productId);
        CartItem cartItem;
        try {
             cartItem   = getCartItem (cartId, productId);
        }
        catch(ResourceNotFoundException e)
        {
            cartItem = new CartItem();
        }
        if(cartItem.getId() == null)
        {
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setUnitPrice(product.getPrice());
        }
        else {
            cartItem.setQuantity (cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {

        Cart cart = cartService.getCart (cartId);
        //1. get Cart
        //2. get Product
        // 3. Get cart item
        // 4. check if present , if present reduce quantity
        //5. calculate price and update
        CartItem cartItem =   getCartItem(cartId, productId);
        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    public void updateItemQuantity(Long cartId, Long productId, int quantity)
    {
        Cart cart = cartService.getCart (cartId);
        cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().ifPresent(item ->
                        {
                            item.setQuantity (quantity);
                            item.setUnitPrice(item.getProduct().getPrice());
                            item.setTotalPrice();
                        }
                );

        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }


    @Override
    public CartItem getCartItem(Long cartId, Long productId)
    {
        Cart cart = cartService.getCart (cartId);
        CartItem cartItem =  cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException ("Item not found here"));
        return cartItem;
    }
}
