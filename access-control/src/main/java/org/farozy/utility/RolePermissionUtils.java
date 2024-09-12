package org.farozy.utility;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.Permission;
import org.farozy.entity.Role;
import org.farozy.entity.RolePermission;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.PermissionRepository;
import org.farozy.repository.RolePermissionRepository;
import org.farozy.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolePermissionUtils {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The role with the specified ID does not exist"
                ));
    }

    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The permission with the specified ID does not exist"
                ));
    }

    public RolePermission getRolePermissionById(Long id) {
        return rolePermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The role permission with the specified ID does not exist"
                ));
    }

}
