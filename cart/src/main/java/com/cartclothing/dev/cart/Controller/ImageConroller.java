package com.cartclothing.dev.cart.Controller;


import com.cartclothing.dev.cart.DTO.ImageDTO;
import com.cartclothing.dev.cart.Exception.ResourceNotFoundException;
import com.cartclothing.dev.cart.Modal.Image;
import com.cartclothing.dev.cart.Response.APIResponse;
import com.cartclothing.dev.cart.Service.Image.IImageService;
import com.cartclothing.dev.cart.Service.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/image")
public class ImageConroller
{

    private final IImageService imageService;

    @PostMapping(value = "/upload")
    public ResponseEntity<APIResponse> saveImage(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDTO> imagesDTOs = imageService.saveImages (files, productId);
            return ResponseEntity.ok (new APIResponse ("Upload Success !", imagesDTOs));
        } catch ( Exception e ) {
            return ResponseEntity.status (INTERNAL_SERVER_ERROR)
                    .body (new APIResponse ("Upload Failed !", e.getMessage ()));
        }
    }

    @GetMapping(value = "/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById (imageId);
            ByteArrayResource resource = new ByteArrayResource (image.getImage ().getBytes (1, (int) image.getImage ().length ()));
            return ResponseEntity.ok ().contentType (MediaType.parseMediaType (image.getFileType ()))
                    .header (HttpHeaders.CONTENT_DISPOSITION, "attachment: filename \""
                            + image.getFileName () + "\"")
                    .body (resource);
        } catch ( Exception e ) {
            throw new RuntimeException (e);
        }
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<APIResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file)
    {
        Image image = imageService.getImageById(imageId);
        if(image != null)
        {
            try {
                imageService.updateImage (file, imageId);
                return ResponseEntity.ok (new APIResponse ("UpdateSuccessfull", null));
            }
            catch( ResourceNotFoundException e)
            {
              return ResponseEntity.status(NOT_FOUND).body(new APIResponse (e.getMessage (), null));
            }
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new APIResponse ("Update Failed", INTERNAL_SERVER_ERROR));

    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<APIResponse> updateImage(@PathVariable Long imageId)
    {
        Image image = imageService.getImageById(imageId);
        if(image != null)
        {
            try {
                imageService.deleteImageById( imageId);
                return ResponseEntity.ok (new APIResponse ("Delete Successfull", null));
            }
            catch( ResourceNotFoundException e)
            {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse (e.getMessage (), null));
            }
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new APIResponse ("Delete Failed", INTERNAL_SERVER_ERROR));

    }
}
