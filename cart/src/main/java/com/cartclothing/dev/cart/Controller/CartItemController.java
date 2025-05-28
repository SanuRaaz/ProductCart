package com.cartclothing.dev.cart.Controller;


import com.cartclothing.dev.cart.Exception.ResourceNotFoundException;
import com.cartclothing.dev.cart.Response.APIResponse;
import com.cartclothing.dev.cart.Service.Cart.ICartItemService;
import com.cartclothing.dev.cart.Service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;

    @PostMapping("/item/add")
    public ResponseEntity<APIResponse> addItemCart(@RequestParam(required = false) Long cartId,  @RequestParam Long productId, @RequestParam  Integer quantity)
    {
        try {
                if(cartId == null || cartId <=  0) {
                    cartId =cartService.initializeNewCart ();
                }
                cartItemService.addItemToCart (cartId, productId, quantity);
            return ResponseEntity.ok(new APIResponse ("Item added successfully", null));
        } catch ( ResourceNotFoundException e ) {
            return ResponseEntity.status(NOT_FOUND).body (new APIResponse (e.getMessage (), null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/")
  public ResponseEntity<APIResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId)
  {
      try {
          cartItemService.removeItemFromCart (cartId, itemId);
          return ResponseEntity.ok(new APIResponse ("Remove Item Success", null));
      } catch ( ResourceNotFoundException e ) {
          return ResponseEntity.status(NOT_FOUND).body (new APIResponse (e.getMessage (), null));
      }
  }

  @PutMapping("/cart/{cartId}/item/{itemId}/update")
  public ResponseEntity<APIResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam Integer quantity)
  {
      try {
          cartItemService.updateItemQuantity (cartId, itemId, quantity);
          return ResponseEntity.ok(new APIResponse ("Updated Quantity", null));
      } catch ( ResourceNotFoundException e ) {
          return ResponseEntity.status(NOT_FOUND).body (new APIResponse (e.getMessage (), null));
      }
  }
}
