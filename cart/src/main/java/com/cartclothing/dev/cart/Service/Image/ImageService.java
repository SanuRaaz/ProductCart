package com.cartclothing.dev.cart.Service.Image;

import com.cartclothing.dev.cart.DTO.ImageDTO;
import com.cartclothing.dev.cart.Modal.Image;
import com.cartclothing.dev.cart.Exception.ResourceNotFoundException;
import com.cartclothing.dev.cart.Modal.Product;
import com.cartclothing.dev.cart.Repository.ImageRepository;
import com.cartclothing.dev.cart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService
{

    @Value ("${api.image.download.prefix}")
    String buildDownlaodUrl;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductRepository productRepository;



    @Override
    public Image getImageById(Long Id) {
        return (imageRepository.findById(Id))
                .orElseThrow(() -> new ResourceNotFoundException("Image is not present for the id : " + Id));
    }

    @Override
    public void deleteImageById(Long id) {

        imageRepository.findById(id).ifPresentOrElse (imageRepository :: delete,
                ()-> {
                    throw new ResourceNotFoundException ("Image is not there");
                }
                );
    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productRepository.getProductById(productId);
        List<Image> imageData = new ArrayList<>();  //issue of the id at the 1.42
        List<ImageDTO> savedImageDTOs =  new ArrayList<>();
        for(MultipartFile file: files)
        {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename ());  // ctrl + alt +t
                image.setFileType(file.getContentType ());
                image.setImage(new SerialBlob (file.getBytes()));
                image.setProduct(product);


                System.out.println (buildDownlaodUrl);
                String downloadUrl =  buildDownlaodUrl + image.getId() ;
                image.setDownloadUrl(downloadUrl);
                imageData.add(image);
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownlaodUrl  + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDTO imageDTO = new ImageDTO ();
                imageDTO.setImageId(savedImage.getId().toString());
                imageDTO.setImageName(savedImage.getFileName());
                imageDTO.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDTOs.add(imageDTO);

            } catch ( SQLException e ) {
                throw new RuntimeException (e.getMessage ());
            } catch ( IOException e ) {
                throw new RuntimeException (e.getMessage ());
            }
        }
        return savedImageDTOs;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
           Image image = getImageById (imageId);
        try {
            image.setFileName(file.getOriginalFilename ());  // ctrl + alt +t
            image.setImage(new SerialBlob (file.getBytes()));
            imageRepository.save(image);
        } catch ( IOException | SQLException e ) {
            throw new RuntimeException (e.getMessage ());
        }

    }

    @Override
    public List<Image> getImageByProductId(Long id) {
     return null;
    }

    @Override
    public List<Image> getAllImage() {
        return imageRepository.findAll ();
    }
}
