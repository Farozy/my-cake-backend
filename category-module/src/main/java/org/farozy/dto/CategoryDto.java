package org.farozy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class CategoryDto {

    public interface CreateGroup {}
    public interface UpdateGroup{}

    @NotBlank(message = "Name is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String name;

    @NotBlank(message = "Description is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String description;

    @NotNull(message = "Image is required", groups = CreateGroup.class)
    @ImageFileSize(groups = {CreateGroup.class, UpdateGroup.class})
    private MultipartFile image;
}
