package org.farozy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private String status;
}
