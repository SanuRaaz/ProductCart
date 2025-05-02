package com.cartclothing.dev.cart.Modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter  //@Data is not recommended to use here as it contains some hashcode and all
//AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Category(String name) {
        this.name = name;
    }

    private String name;

    @OneToMany( mappedBy = "category")
    @JsonIgnore  // break the loop
     //@JsonManagedReference
    //mappedBy = "category", should not be used as the category and product is independent entity, the deletion of the category should not ef
    //effect the deletion of the product. therefore the orphan removal should also be restricted
    private List<Product> products;
}
