package com.cartclothing.dev.cart.Modal;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    //orphan removal would be the deletion of the cartitem automatically
    private Set<CartItem>  items;

    public void addItem(CartItem item)
    {
        this.items.add(item);
        updateTotalAmount ();
    }


    public void removeItem(CartItem item)
    {
        this.items.remove(item);
      //  this.setCartItem = null;
        updateTotalAmount();
    }

    private void updateTotalAmount()
    {
        this.totalAmount = items.stream().map(
                item -> {
                    BigDecimal unitPrice = item.getUnitPrice ();
                    if ( unitPrice == null ) {
                        return BigDecimal.ZERO;
                    }
                    return unitPrice.multiply (BigDecimal.valueOf (item.getQuantity ()));
                }).reduce(BigDecimal.ZERO, BigDecimal :: add);

    }
}
