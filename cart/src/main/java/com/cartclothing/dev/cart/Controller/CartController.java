package com.cartclothing.dev.cart.Controller;


import com.cartclothing.dev.cart.Exception.ResourceNotFoundException;
import com.cartclothing.dev.cart.Modal.Cart;
import com.cartclothing.dev.cart.Response.APIResponse;
import com.cartclothing.dev.cart.Service.Cart.CartService;
import com.cartclothing.dev.cart.Service.Cart.ICartItemService;
import com.cartclothing.dev.cart.Service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping(value ="${api.prefix}/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/cart/{cartId}/my-cart")
    public ResponseEntity<APIResponse> getCart(@PathVariable Long cartId)
    {
        try {
            Cart cart = cartService.getCart (cartId);
            if(cart.getId() == null)
            {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse ("Not found", null));
            }
            return ResponseEntity.ok(new APIResponse ("Found", cart));
        } catch ( ResourceNotFoundException e ) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse (e.getMessage (), null));
        }
    }


    @DeleteMapping("/{cartId}/clear")
    public  ResponseEntity<APIResponse> clearCart(@PathVariable Long cartId)
    {
        try {
            cartService.clearCart (cartId);
            return ResponseEntity.ok(new APIResponse ("CLear Cart Success !", null));
        }
        catch ( ResourceNotFoundException e )
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse ("Resource not found", null));
        }
    }


    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<APIResponse> getTotalPrice(@PathVariable Long cartId)
    {
        try {
            BigDecimal totalPrice = cartService.getTotalPrice (cartId);
            return ResponseEntity.ok(new APIResponse ("Total Price", totalPrice));
        } catch ( ResourceNotFoundException e ) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse (e.getMessage (), null));
        }
    }

}
