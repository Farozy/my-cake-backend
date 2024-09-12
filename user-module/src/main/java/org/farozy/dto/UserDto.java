package org.farozy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farozy.validation.CreateGroup;
import org.farozy.validation.UpdateGroup;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.farozy.validation.annotation.image.ImageFileType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Builder
public class UserDto {

    @NotBlank(message = "Username is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String username;

    @NotBlank(message = "Email is required", groups = {CreateGroup.class, UpdateGroup.class})
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required", groups = {CreateGroup.class})
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Firstname is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String firstName;

    @NotBlank(message = "Lastname is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String lastName;

    private LocalDate dateOfBirth;

    private String address;

    private String phoneNumber;

    @NotBlank(message = "Role is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String role;

    @ImageFileSize
    @ImageFileType
    private MultipartFile image;

}