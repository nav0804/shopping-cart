package com.nav.shoppingcart.services;

import com.nav.shoppingcart.dto.ImageDto;
import com.nav.shoppingcart.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> file, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
