package org.farozy.controller;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.FoodImagesUploadDto;
import org.farozy.entity.FoodImage;
import org.farozy.helper.FileUploadProperties;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.FoodImageService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/food/image")
@RequiredArgsConstructor
public class FoodImageController {

    private final FoodImageService foodImageService;
    private final FileUploadProperties fileUploadProperties;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodImage>> findById(@PathVariable Long id) {
        FoodImage getFoodImageById = foodImageService.findById(id);
        String successMessage = "Get food image by ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, getFoodImageById);
    }

    @GetMapping("/food-id/{id}")
    public ResponseEntity<ApiResponse<List<FoodImage>>> getByFoodId(@PathVariable Long id) {
        List<FoodImage> getImageByFoodId = foodImageService.findByFoodId(id);
        String successMessage = "Get food image by food ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, getImageByFoodId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FoodImage>> uploadImages(
            FoodImagesUploadDto request, @RequestParam("images") List<MultipartFile> images
    ) {
        if (images.size() > fileUploadProperties.getMaxFoodImage()) {
            return ResponseHelper.buildResponseData(
                    HttpStatus.BAD_REQUEST, "Max. 5 file image for upload", null
            );
        }

        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                return ResponseHelper.buildResponseData(
                        HttpStatus.BAD_REQUEST, "One of the file images is empty", null
                );
            }
        }

        foodImageService.uploadImages(request, images);
        String successMessage = "Upload images successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodImage>> update(
            @PathVariable Long id, FoodImagesUploadDto request, MultipartFile image
    ) {
        FoodImage updatedFoodImageById = foodImageService.updateUploadImage(id, request, image);
        String successMessage = "Update food image successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, updatedFoodImageById);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodImage>> destroy(@PathVariable Long id) {
        foodImageService.delete(id);
        String successMessage = "Food Images deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

    @DeleteMapping("/food-id/{id}")
    public ResponseEntity<ApiResponse<FoodImage>> destroyByFoodId(@PathVariable Long id) {
        foodImageService.deleteByFoodId(id);

        String successMessage = "Food images by food ID deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

}
