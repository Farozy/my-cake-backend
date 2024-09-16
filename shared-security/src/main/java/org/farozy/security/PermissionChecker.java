package org.farozy.security;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.Permission;
import org.farozy.entity.Role;
import org.farozy.entity.RolePermission;
import org.farozy.entity.User;
import org.farozy.exception.AccessDeniedException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.PermissionRepository;
import org.farozy.repository.RolePermissionRepository;
import org.farozy.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PermissionChecker {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;

    public boolean hasPermission(Authentication authentication, String permission) {

        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        }

        if (authentication.getAuthorities() == null) {
            throw new IllegalArgumentException("Authentication authorities cannot be null");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Role> roles = user.getRoles();

        if (roles == null || roles.isEmpty()) throw new ResourceNotFoundException("Roles not found");

        Permission foundPermission = permissionRepository.findByName(permission)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        for (Role role : roles) {
            Optional<RolePermission> rolePermission = rolePermissionRepository
                    .findByRoleIdAndPermissionId(role.getId(), foundPermission.getId());

            if (rolePermission.isPresent()) return true;
        }

        throw new AccessDeniedException("User does not have the required permission");
    }

}
