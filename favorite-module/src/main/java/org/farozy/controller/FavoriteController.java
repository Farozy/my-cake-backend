package org.farozy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.FavoriteDto;
import org.farozy.entity.Favorite;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<ApiResponse<Favorite>> addFavorite(@Valid @RequestBody FavoriteDto request) {
        Favorite favorite = favoriteService.addFavorite(request);
        String message = "Added favorite successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, favorite);
    }

    @PatchMapping("/{favoriteId}")
    public ResponseEntity<ApiResponse<Favorite>> updateFavorite(
            @PathVariable Long favoriteId, @Valid @RequestBody FavoriteDto request
    ) {
        Favorite updatedFavorite = favoriteService.updateFavorite(favoriteId, request);
        String message = "Updated favorite successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, updatedFavorite);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Favorite>>> getFavoritesByUserId(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
        String message = "Fetched favorite by user ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, favorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ApiResponse<Favorite>> destroyFavoriteById(@PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        String message = "Deleted favorite by ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, null);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Favorite>> destroyFavoriteByUserId(@PathVariable Long userId) {
        favoriteService.deleteFavoriteByUserId(userId);
        String message = "Deleted favorite by user ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, null);
    }
}
