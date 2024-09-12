package org.farozy.payload;

import org.farozy.entity.Permission;
import org.farozy.entity.Role;
import java.util.List;

public record RolePermissionResponse(Role role, List<Permission> permissions) {
}
