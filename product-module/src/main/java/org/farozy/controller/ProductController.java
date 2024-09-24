package org.farozy.controller;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.ProductDto;
import org.farozy.entity.Product;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@Validated
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> retrieveAll() {
        List<Product> products = productService.findAll();
        String message = "Products retrieved all successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> retrieveById(@PathVariable Long id) {
        Product foundProductById = productService.findById(id);
        String message = "Product retrieved by id successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, foundProductById);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> create(
            @Validated(ProductDto.CreateGroup.class) @ModelAttribute ProductDto request,
            @RequestParam("image") MultipartFile image
    ) {
        if (image.isEmpty()) {
            return ResponseHelper.buildResponseData(HttpStatus.BAD_REQUEST, "Image is required", null);
        }

        Product createdProduct = productService.create(request, image);
        String message = "Product created successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, createdProduct);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> update(
            @PathVariable Long id, @Validated(ProductDto.UpdateGroup.class) @ModelAttribute ProductDto request,
            @RequestParam(value = "image", required = false) MultipartFile file
    ) {
        Product updatedProduct = productService.update(id, request, file);
        String message = "Product updated successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> destroy(@PathVariable Long id) {
        productService.delete(id);
        String message = "Product deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

}
