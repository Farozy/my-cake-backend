package org.farozy.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class FoodDto {

    public interface CreateGroup {}
    public interface UpdateGroup{}

    @NotBlank(message = "Name is required", groups = {FoodDto.CreateGroup.class, FoodDto.UpdateGroup.class})
    private String name;

    @NotNull(message = "Price is required", groups = {FoodDto.CreateGroup.class, FoodDto.UpdateGroup.class})
    private BigDecimal price;

    @NotBlank(message = "Description is required", groups = {FoodDto.CreateGroup.class, FoodDto.UpdateGroup.class})
    private String description;

    @NotNull(message = "Stock is required", groups = {FoodDto.CreateGroup.class, FoodDto.UpdateGroup.class})
    private Integer stock;

    @NotNull(message = "Available is required", groups = {FoodDto.CreateGroup.class, FoodDto.UpdateGroup.class})
    private Boolean available;

    @Digits(integer = 5, fraction = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @ImageFileSize(groups = {FoodDto.CreateGroup.class, FoodDto.UpdateGroup.class})
    private MultipartFile image;

    @NotNull(message = "Category ID is required", groups = {FoodDto.CreateGroup.class, FoodDto.UpdateGroup.class})
    private Long categoryId;

    @NotNull(message = "Store ID is required", groups = {FoodDto.CreateGroup.class, FoodDto.UpdateGroup.class})
    private Long storeId;

}
