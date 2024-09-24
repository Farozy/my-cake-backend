package org.farozy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class ProductImageDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @ImageFileSize()
    private MultipartFile image;

}
