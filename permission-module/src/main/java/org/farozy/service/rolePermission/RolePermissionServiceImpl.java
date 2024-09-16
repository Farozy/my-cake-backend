package org.farozy.service.rolePermission;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.RolePermissionDto;
import org.farozy.entity.Permission;
import org.farozy.entity.Role;
import org.farozy.entity.RolePermission;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.payload.PermissionRoleResponse;
import org.farozy.payload.RolePermissionResponse;
import org.farozy.repository.RolePermissionRepository;
import org.farozy.utility.RolePermissionUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RolePermissionUtils rolePermissionUtils;

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<RolePermission> findAll() {
        try {
            return rolePermissionRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching role permission: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public RolePermission findById(Long id) {
        try {
            return rolePermissionUtils.getRolePermissionById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public PermissionRoleResponse findRoleByPermissionId(Long id) {
        try {
            Permission permission = rolePermissionUtils.getPermissionById(id);

            List<Role> roles = rolePermissionRepository.findRoleByPermission(permission)
                    .stream().map(RolePermission::getRole)
                    .collect(Collectors.toList());

            return new PermissionRoleResponse(permission, roles);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public RolePermissionResponse findPermissionByRoleId(Long id) {
        try {
            Role role = rolePermissionUtils.getRoleById(id);

            List<Permission> permissions = rolePermissionRepository.findPermissionByRole(role)
                    .stream().map(RolePermission::getPermission)
                    .collect(Collectors.toList());

            return new RolePermissionResponse(role, permissions);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserCreate
    public RolePermission save(RolePermissionDto request) {
        try {
            Role role = rolePermissionUtils.getRoleById(request.getRoleId());
            Permission permission = rolePermissionUtils.getPermissionById(request.getPermissionId());

            processCheckingRolePermission(role.getId(), permission.getId());

            return saveOrUpdateRolePermission(null, role, permission);
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving role permission: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserUpdate
    public RolePermission update(Long id, RolePermissionDto request) {
        try {
            rolePermissionUtils.getRolePermissionById(id);

            Role role = rolePermissionUtils.getRoleById(request.getRoleId());
            Permission permission = rolePermissionUtils.getPermissionById(request.getPermissionId());

            processCheckingRolePermission(role.getId(), permission.getId());

            return saveOrUpdateRolePermission(id, role, permission);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating role permission: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserDelete
    public void delete(Long id) {
        try {
            rolePermissionUtils.getRolePermissionById(id);

            rolePermissionRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting role permission:" + ex.getMessage());
        }
    }

    private void processCheckingRolePermission(Long roleId, Long permissionId) {
        rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)
                .ifPresent(rolePermission -> {
                    throw new ResourceAlreadyExistsException(
                            String.format("The role with ID %s already has the permission with ID %s",
                                    rolePermission.getRole().getId(), rolePermission.getPermission().getId()
                            )
                    );
                });
    }

    private RolePermission saveOrUpdateRolePermission(Long id, Role role, Permission permission) {
        RolePermission rolePermission = (id == null)
                ? new RolePermission()
                : rolePermissionUtils.getRolePermissionById(id);

        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        return rolePermissionRepository.save(rolePermission);
    }

}
