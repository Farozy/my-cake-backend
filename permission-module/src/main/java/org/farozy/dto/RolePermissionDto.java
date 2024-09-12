package org.farozy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RolePermissionDto {

    @NotNull(message = "Role ID is required")
    @Min(value = 1, message = "Role ID must be greater than or equal to 1")
    private Long roleId;

    @NotNull(message = "Permission ID is required")
    @Min(value = 1, message = "Permission ID must be greater than or equal to 1")
    private Long permissionId;

}
