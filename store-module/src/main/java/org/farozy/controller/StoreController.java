package org.farozy.controller;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.StoreDto;
import org.farozy.entity.Store;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Store>>> retrieveAll() {
        List<Store> stores = storeService.findAll();
        String message = "Stores retrieved all successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, stores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Store>> findById(@PathVariable Long id) {
        Store store = storeService.findById(id);
        String message = "Store by ID retrieved successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, store);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Store>> create(
            @Validated(StoreDto.CreateGroup.class) @ModelAttribute StoreDto request,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        Store createdStore = storeService.save(request, multipartFile);
        String message = "Store created successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, createdStore);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Store>> edit(
            @PathVariable Long id, @Validated(StoreDto.UpdateGroup.class) @ModelAttribute StoreDto request,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        Store updatedStore = storeService.update(id, request, multipartFile);
        String message = "Store updated successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, updatedStore);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Store>> destroy(@PathVariable Long id) {
        storeService.delete(id);
        String successMessage = "Store deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, successMessage, null);
    }

}
