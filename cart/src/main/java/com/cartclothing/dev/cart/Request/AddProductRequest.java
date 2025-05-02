package com.cartclothing.dev.cart.Request;

import com.cartclothing.dev.cart.Modal.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.nio.file.Path;

// @Getter & @Setter can be replaced with the @Data anotaion

@Data  // can be used as no manipulation in database
public class AddProductRequest {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;  // price shoulb in BigDecimal
    private int inventory;
    private Category category;


}
