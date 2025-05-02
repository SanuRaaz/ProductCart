package com.cartclothing.dev.cart.Service.Product;

import com.cartclothing.dev.cart.DTO.ImageDTO;
import com.cartclothing.dev.cart.DTO.ProductDTO;
import com.cartclothing.dev.cart.Exception.ProductNotFoundException;
import com.cartclothing.dev.cart.Modal.Product;
import com.cartclothing.dev.cart.Modal.Image;
import com.cartclothing.dev.cart.Modal.Category;
import com.cartclothing.dev.cart.Repository.CategoryRepository;
import com.cartclothing.dev.cart.Repository.ImageRepository;
import com.cartclothing.dev.cart.Repository.ProductRepository;
import com.cartclothing.dev.cart.Request.AddProductRequest;
import com.cartclothing.dev.cart.Request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
//@RequiredArgsConstructor
   // can be used for the injection of the object
public class ProductService implements IProductService{
   // private final ProductRepository productRepository;

    @Autowired
  private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageRepository imageRepository;
  /*
  public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }    due to the Requeired all args constructor introduced
*/
    @Override
    public Product addProduct(AddProductRequest request) {
        //check if category is found
        //if yes set it as new product category
        // if no, then save it as new category
        // then set it as product new category
        Category category = Optional.ofNullable (categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(
                        ()->{
                            Category newCategory = new Category(request.getCategory().getName());
                            return categoryRepository.save(newCategory);
                        }
                );
        request.setCategory(category);
       // Product temp = createProduct(request, category );
        return productRepository.save(createProduct(request, category ) );
    }

    private Product createProduct(AddProductRequest request,  Category category)
    {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category
                );
    }  //heppa method used for the cretion of the hierarchy inside of the method

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById (id)
                .orElseThrow(() -> new ProductNotFoundException ("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository :: delete,
                () -> {throw new ProductNotFoundException("Product not found for the deletion");});
   // if present delete the record
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
         return productRepository.findById(productId)
                 .map(existingProduct-> updateExistingProduct (existingProduct, request))
                 .map(productRepository :: save)
                 .orElseThrow(() -> new ProductNotFoundException ("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request)
    {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        Category category =  categoryRepository.findByName (request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
              //  .orElseThrow(()-> new ProductNotFoundException ("Product is present by this category"));
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByBrandAndCategory(String brand, String category) {
        return productRepository.findByBrandAndCategoryName(brand, category);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName (name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDTO> getConvertedProducts(List<Product> products)
    {
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDTO convertToDto(Product product)
    {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        //list of image to imagedto
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDTO> imageDTOs = images.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .toList ();
             //  .collect(Collectors.toList());
        productDTO.setImages(imageDTOs);
        return productDTO;
    }
}
