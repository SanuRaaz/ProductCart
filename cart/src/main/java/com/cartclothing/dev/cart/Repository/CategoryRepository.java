package com.cartclothing.dev.cart.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cartclothing.dev.cart.Modal.Category;

import java.util.List;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long> {
    Category findByName(String  name);
    boolean existsByName(String name);
}
