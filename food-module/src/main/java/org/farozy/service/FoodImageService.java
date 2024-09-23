package org.farozy.service;

import org.farozy.dto.FoodImagesUploadDto;
import org.farozy.entity.FoodImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodImageService {

    FoodImage findById(Long id);

    List<FoodImage> findByFoodId(Long id);

    void uploadImages(FoodImagesUploadDto request, List<MultipartFile> images);

    FoodImage updateUploadImage(Long id, FoodImagesUploadDto request, MultipartFile image);

    void delete(Long id);

    void deleteByFoodId(Long id);

}
