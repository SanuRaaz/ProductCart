package com.cartclothing.dev.cart.Controller;

import com.cartclothing.dev.cart.DTO.ProductDTO;
import com.cartclothing.dev.cart.Exception.ProductNotFoundException;
import com.cartclothing.dev.cart.Modal.Product;
import com.cartclothing.dev.cart.Request.AddProductRequest;
import com.cartclothing.dev.cart.Request.ProductUpdateRequest;
import com.cartclothing.dev.cart.Response.APIResponse;
import com.cartclothing.dev.cart.Service.Product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/product")
public class ProductController {

    private final IProductService productService;

    @GetMapping(value = "product/getAll")
    public ResponseEntity<APIResponse> getALLProducts()
    {
        try {
            List<Product> productList = productService.getAllProduct ();
            List<ProductDTO> convertedProducts = productService.getConvertedProducts (productList);
            if(productList.isEmpty ())
            {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse ("Not Found", null));
            }
            return ResponseEntity.ok(new APIResponse ("Success", convertedProducts));
            }
        catch ( Exception e ) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResponse ("Issue occured while fetching" + e.getMessage(), null));
        }
    }

    @GetMapping(value = "/product/{id}/product")
    public ResponseEntity<APIResponse> getProductById(@PathVariable  Long id)
    //use same  used in url or @pathvarial (id) Long productid)
    {
        try {
            Product product = productService.getProductById (id);
            ProductDTO productDTO = productService.convertToDto (product);
            return ResponseEntity.ok(new APIResponse ("Found", productDTO));
        } catch ( ProductNotFoundException e )
        {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResponse (e.getMessage (), null));
        }
        catch(Exception e)
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResponse (e.getMessage (), null));
        }
    }

    @DeleteMapping(value = "/product/{id}/delete")
    public ResponseEntity<APIResponse> deleteById(@PathVariable Long id)
    {
        try {
            productService.deleteProductById (id);
            return ResponseEntity.ok(new APIResponse ("Deleted Successfully", null));
        } catch ( ProductNotFoundException e ) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResponse (e.getMessage (), null));
        }
    }

    @PostMapping("/product/addproduct")
    public ResponseEntity<APIResponse> addProduct(@RequestBody AddProductRequest product) // if tge requestbody annotation is not used not value will be feed into the db
    {
        try {
            Product theProduct = productService.addProduct (product);
            return ResponseEntity.ok(new APIResponse ("Added Successfully", theProduct));
        } catch ( Exception e ) {
            throw new RuntimeException (e);
        }
    }

    @PutMapping(value = "/product/{id}/update")
    public ResponseEntity<APIResponse> updateResponse(@RequestBody ProductUpdateRequest product, @PathVariable Long id)
    {
        try {
            Product theProduct = productService.updateProduct (product, id);
            ProductDTO productDTO = productService.convertToDto (theProduct);
            return ResponseEntity.ok(new APIResponse ("Found", productDTO));
        } catch ( ProductNotFoundException e ) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResponse (e.getMessage (), null));
        }
    }

@GetMapping(value = "/products/by/brand-and-name/{brandName}/{productName}")
    public ResponseEntity<APIResponse> getProductByBrandAndName(@PathVariable String brandName, @PathVariable String productName)
    {
        try {
            List<Product> products = productService.getProductByBrandAndName (brandName, productName);
            List<ProductDTO> productDTOs = productService.getConvertedProducts (products);
            return ResponseEntity.ok(new APIResponse ("Success", productDTOs));
        } catch ( Exception e ) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse (e.getMessage (), null));
        }
    }

    @GetMapping(value  = "/products/by/category-and-brand/")
    public ResponseEntity<APIResponse> getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand)
    {
        try {
            List<Product> products = productService.getProductByBrandAndCategory (category, brand);
            List<ProductDTO> productDTOs = productService.getConvertedProducts (products);
            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse ("No Product Found", null));
            }
            return ResponseEntity.ok(new APIResponse ("Success", productDTOs));
        } catch ( Exception e ) {
              return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse (e.getMessage (), null));
        }
    }

    @GetMapping(value  = "/products/by/name/")
    public ResponseEntity<APIResponse> getProductByName(@RequestParam String name)
    {
        try {
            List<Product> products = productService.getProductByName (name);
            List<ProductDTO> productDTOs = productService.getConvertedProducts (products);

            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse ("No Product Found", null));
            }
            return ResponseEntity.ok(new APIResponse ("Success", productDTOs));
        } catch ( Exception e ) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse (e.getMessage (), null));
        }
    }

    @GetMapping(value = "/product/{category}/all/products")
    public ResponseEntity<APIResponse> findProductByCategory(@PathVariable String category)
    {
        try{
            List<Product> products = productService.getAllProductByCategory (category);
            List<ProductDTO> productDTOs = productService.getConvertedProducts (products);

            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse ("No Products Found", null));
            }
            return ResponseEntity.ok(new APIResponse ("Found", productDTOs));
        }
        catch(ProductNotFoundException e)
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse (e.getMessage (), null));
        }

    }

    @GetMapping(value = "/product/by-brand")
    public ResponseEntity<APIResponse> findProductByBrand(@RequestParam String brand)
    {
        try{
            List<Product> products = productService.getProductByBrand (brand);
            List<ProductDTO> productDTOs = productService.getConvertedProducts (products);

            //we can use var also, it is mapped by value type
            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse ("No Products Found", null));
            }
            return ResponseEntity.ok(new APIResponse ("found", productDTOs));

        }
        catch(ProductNotFoundException e)
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse (e.getMessage (), null));
        }

    }

    //Long countProductByBrandAndName(String brand, String name);

    @GetMapping(value = "/product/countproduct/by/brand-name")
    public ResponseEntity<APIResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name)
    {
        try {
            var  answer = productService.countProductByBrandAndName (brand, name);
            return ResponseEntity.ok(new APIResponse ("product count: ", answer));
           }
        catch ( Exception e )
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse (e.getMessage (), null));
        }
    }
}
