package org.farozy.controller;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.FoodDto;
import org.farozy.entity.Food;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<Food>>> retrieveAll() {
        List<Food> foods = foodService.findAll();
        String message = "Foods retrieved all successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, foods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Food>> findById(@PathVariable Long id) {
        Food food = foodService.findById(id);
        String message = "Food by ID retrieved successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, food);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Food>> create(
            @Validated(FoodDto.CreateGroup.class) @ModelAttribute FoodDto request,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        if (multipartFile.isEmpty()) {
            return ResponseHelper.buildResponseData(HttpStatus.BAD_REQUEST, "Image is required", null);
        }

        Food createdFood = foodService.save(request, multipartFile);
        String message = "Food created successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, createdFood);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Food>> edit(
            @PathVariable Long id, @Validated(FoodDto.UpdateGroup.class) @ModelAttribute FoodDto request,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        Food updatedFood = foodService.update(id, request, multipartFile);
        String message = "Food updated successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, updatedFood);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Food>> destroy(@PathVariable Long id) {
        foodService.delete(id);
        String successMessage = "Food deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

}
