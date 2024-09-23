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
public class FoodImagesUploadDto {

    @NotNull(message = "Food ID is required")
    private Long foodId;

    @ImageFileSize()
    private MultipartFile image;

}
