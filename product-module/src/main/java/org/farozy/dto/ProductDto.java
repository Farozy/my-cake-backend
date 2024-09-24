package org.farozy.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Builder
public class ProductDto {

    public interface CreateGroup {}
    public interface UpdateGroup{}

    @NotBlank(message = "Name is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String name;

    @NotBlank(message = "Description is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String description;

    @NotNull(message = "Image id is required", groups = {CreateGroup.class})
    @ImageFileSize(groups = {CreateGroup.class, UpdateGroup.class})
    private MultipartFile image;

    @NotNull(message = "category id is required", groups = {CreateGroup.class, UpdateGroup.class})
    private Long categoryId;

    @NotNull(message = "store id is required", groups = {CreateGroup.class, UpdateGroup.class})
    private Long storeId;

    @NotNull(message = "Price is required", groups = {CreateGroup.class, UpdateGroup.class})
    @Positive(message = "Price must be positive", groups = {CreateGroup.class, UpdateGroup.class})
    private String price;

    @NotNull(message = "Stock is required", groups = {CreateGroup.class, UpdateGroup.class})
    @Min(value = 1, message = "Stock must be at least 1", groups = {CreateGroup.class, UpdateGroup.class})
    @Max(value = 100000, message = "Stock must be less than or equal to 100000", groups = {CreateGroup.class, UpdateGroup.class})
    private Integer stock;

    private BigDecimal discount;

}
