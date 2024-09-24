package org.farozy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.ProductImageDto;
import org.farozy.entity.ProductImage;
import org.farozy.helper.FileUploadProperties;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.productImage.ProductImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product/image")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;
    private final FileUploadProperties fileUploadProperties;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImage>> findById(@PathVariable Long id) {
        ProductImage getProductImageById = productImageService.findById(id);
        String successMessage = "Get product image by ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, getProductImageById);
    }

    @GetMapping("/product-id/{id}")
    public ResponseEntity<ApiResponse<List<ProductImage>>> getByProductId(@PathVariable Long id) {
        List<ProductImage> getImageByProductId = productImageService.findByProductId(id);
        String successMessage = "Get product image by product ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, getImageByProductId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductImage>> uploadImages(
            ProductImageDto request, @RequestParam("images") List<MultipartFile> images
    ) {
        if (images.size() > fileUploadProperties.getMaxProductImage()) {
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

        productImageService.uploadProductImage(request, images);
        String successMessage = "Upload images successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImage>> update(
            @PathVariable Long id, @Valid ProductImageDto request,
            @RequestParam("image") MultipartFile image
    ) {
        if (image.isEmpty()) {
            return ResponseHelper.buildResponseData(HttpStatus.BAD_REQUEST, "Image is required", null);
        }

        ProductImage updatedProductImageById = productImageService.updateProductImage(id, request, image);
        String successMessage = "Update product image successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, updatedProductImageById);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImage>> destroy(@PathVariable Long id) {
        productImageService.delete(id);
        String successMessage = "Product Images deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

    @DeleteMapping("/product-id/{id}")
    public ResponseEntity<ApiResponse<ProductImage>> destroyByProductId(@PathVariable Long id) {
        productImageService.deleteByProductId(id);
        String successMessage = "Product images by product ID deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

}