package com.cartclothing.dev.cart.Repository;

import com.cartclothing.dev.cart.Modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cartclothing.dev.cart.Modal.Image;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
List<Image> findImageByProduct(Optional<Product> product);
    List<Image> findByProductId(Long id);
}
