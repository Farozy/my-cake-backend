package org.farozy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class StoreDto {

    public interface CreateGroup {}
    public interface UpdateGroup{}

    @NotBlank(message = "Name is required", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(max = 255, message = "Name must be at most 255 characters long")
    private String name;

    @NotBlank(message = "Description is required", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(max = 500, message = "Descritionl must be at most 500 characters long")
    private String description;

    @NotBlank(message = "Address is required", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(max = 500, message = "Address must be at most 500 characters long")
    private String address;

    @NotNull(message = "Opening time is requeired", groups = {CreateGroup.class, UpdateGroup.class})
    private LocalTime openingTime;

    @NotNull(message = "Closing time is required", groups = {CreateGroup.class, UpdateGroup.class})
    private LocalTime closingTime;

    @ImageFileSize(groups = {CreateGroup.class, UpdateGroup.class})
    private MultipartFile image;

    private BigDecimal latitude;

    private BigDecimal longitude;

}
