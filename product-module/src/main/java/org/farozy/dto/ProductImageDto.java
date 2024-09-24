package org.farozy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.farozy.validation.annotation.image.ImageFileType;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class ProductImageDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Image id is required")
    @ImageFileSize
    @ImageFileType
    private MultipartFile image;

}
