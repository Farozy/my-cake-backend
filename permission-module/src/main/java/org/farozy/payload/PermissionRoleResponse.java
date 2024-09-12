package org.farozy.payload;

import org.farozy.entity.Permission;
import org.farozy.entity.Role;

import java.util.List;

public record PermissionRoleResponse(Permission permission, List<Role> roles) {
}

