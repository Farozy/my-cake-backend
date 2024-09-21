package org.farozy.controller;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.CategoryDto;
import org.farozy.entity.Category;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> retrieveAll() {
        List<Category> categories = categoryService.findAll();
        String message = "Categories retrieved all successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        String message = "Category by ID retrieved successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, category);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> create(
            @Validated(CategoryDto.CreateGroup.class) @ModelAttribute CategoryDto request,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        Category createdCategory = categoryService.save(request, multipartFile);
        String message = "Category created successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, createdCategory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> edit(
            @PathVariable Long id, @Validated(CategoryDto.UpdateGroup.class) @ModelAttribute CategoryDto request,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        Category updatedCategory = categoryService.update(id, request, multipartFile);
        String message = "Category updated successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> destroy(@PathVariable Long id) {
        categoryService.delete(id);
        String successMessage = "Category deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

}
