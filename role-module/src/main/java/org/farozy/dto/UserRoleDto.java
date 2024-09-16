package org.farozy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRoleDto {

    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be greater than or equal to 1")
    private Long userId;

    @NotNull(message = "Role ID is required")
    @Min(value = 1, message = "Role ID must be greater than or equal to 1")
    private Long roleId;

}