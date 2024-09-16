package org.farozy.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.farozy.validation.annotation.image.ImageFileType;
import org.farozy.validation.annotation.number.ValidWhatsAppNumber;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Builder
public class RegistrationDto {

//    @NotBlank(message = "First name is required")
//    private String firstName;
//
//    @NotBlank(message = "Last name is required")
//    private String lastName;
//
//    @NotBlank(message = "Username is required")
//    private String username;
//
//    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @ValidWhatsAppNumber
    private String whatsAppNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @ImageFileSize
    @ImageFileType
    private MultipartFile image;

}
