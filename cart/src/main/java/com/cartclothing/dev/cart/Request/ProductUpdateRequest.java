package com.cartclothing.dev.cart.Request;

import com.cartclothing.dev.cart.Modal.Category;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;  // price shoulb in BigDecimal
    private int inventory;
    private Category category;
}
