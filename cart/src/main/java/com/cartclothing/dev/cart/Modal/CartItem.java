package com.cartclothing.dev.cart.Modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private BigDecimal  unitPrice;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    //@JsonIgnore
    @JoinColumn(name = "cartid")
    private Cart cart;

    @PostUpdate
    public void setTotalPrice()
    {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal (quantity));
    }

}
