package com.cartclothing.dev.cart.DTO;


import com.cartclothing.dev.cart.Modal.Category;
import com.cartclothing.dev.cart.Modal.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;  // price shoulb in BigDecimal
    private int inventory;

    private Category category;
    private List<ImageDTO> images;
}


