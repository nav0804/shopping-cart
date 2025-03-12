package com.nav.shoppingcart.services.impl;

import com.nav.shoppingcart.dto.ImageDto;
import com.nav.shoppingcart.entities.Image;
import com.nav.shoppingcart.entities.Product;
import com.nav.shoppingcart.exceptions.ResourceNotFoundException;
import com.nav.shoppingcart.repository.ImageRepository;
import com.nav.shoppingcart.services.IImageService;
import com.nav.shoppingcart.services.IProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;

    public ImageService(ImageRepository imageRepository, IProductService productService){
        this.imageRepository = imageRepository;
        this.productService = productService;
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, ()->{
            throw new ResourceNotFoundException("Image not found");
        });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product existingProduct = productService.getProductById(productId);
        List<ImageDto> dto = new ArrayList<>();
        for(MultipartFile file:files){
            try{

                /*
                * Creating new image for every file
                */
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(existingProduct);


                String downloadUrl = "api/v1/images/image/download";

                /*
                 * Generating the download Url for every image.
                 * Saving the image and then again updating the download URL by adding the new id to it.
                 * Again saving the updated download url for it
                 */

                String url = downloadUrl + image.getId();
                image.setDownloadUrl(url);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(downloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                /*
                * Creating a list of dto which contains the list of images for a product and returning it
                * */
                ImageDto newDto = new ImageDto();
                newDto.setImageName(savedImage.getFileName());
                newDto.setImageId(savedImage.getId());
                newDto.setDownloadUrl(savedImage.getDownloadUrl());
                dto.add(newDto);

            }catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return dto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
        }catch(IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
