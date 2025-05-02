package com.cartclothing.dev.cart.Service.Image;

import com.cartclothing.dev.cart.DTO.ImageDTO;
import com.cartclothing.dev.cart.Modal.Image;
import com.cartclothing.dev.cart.Modal.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long Id);
    void deleteImageById(Long id);
    List<ImageDTO> saveImages(List<MultipartFile> file, Long productId);  //image is multipart
    void updateImage(MultipartFile file, Long imageId);

    List<Image> getImageByProductId(Long Id);
    List<Image> getAllImage();
  //  List<Image> findImageByProduct(Product product);
}
