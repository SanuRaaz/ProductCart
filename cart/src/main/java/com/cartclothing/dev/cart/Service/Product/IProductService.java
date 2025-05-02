package com.cartclothing.dev.cart.Service.Product;

import com.cartclothing.dev.cart.DTO.ProductDTO;
import com.cartclothing.dev.cart.Modal.Product;
import com.cartclothing.dev.cart.Request.AddProductRequest;
import com.cartclothing.dev.cart.Request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProduct();
    List<Product> getAllProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByBrandAndCategory(String brand, String category);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String category, String name);
     Long countProductByBrandAndName(String brand, String name);
    public ProductDTO convertToDto(Product product);
    public List<ProductDTO> getConvertedProducts(List<Product> products);

}
