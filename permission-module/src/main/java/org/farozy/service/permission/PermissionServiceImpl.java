package org.farozy.service.permission;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.PermissionDto;
import org.farozy.entity.Permission;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.PermissionRepository;
import org.farozy.utility.RolePermissionUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionUtils rolePermissionUtils;

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<Permission> findAll() {
        try {
            return permissionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching permissions: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Permission findById(Long id) {
        try {
            return rolePermissionUtils.getPermissionById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching permission by ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserCreate
    public Permission save(PermissionDto request) {
        try {
            return saveOrUpdatePermission(null, request);
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving permission: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserUpdate
    public Permission update(Long id, PermissionDto request) {
        try {
            return saveOrUpdatePermission(id, request);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while updating permission: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserDelete
    public void delete(Long id) {
        try {
            rolePermissionUtils.getPermissionById(id);

            permissionRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting permission: " + e.getMessage());
        }
    }

    private Permission saveOrUpdatePermission(Long id, PermissionDto request) {
        Permission permission = (id == null) ? new Permission() : rolePermissionUtils.getPermissionById(id);

        permissionRepository.findByName(request.getName())
                .filter(existingCategory -> !existingCategory.getId().equals(id))
                .ifPresent(cate -> {
                    throw new ResourceAlreadyExistsException(
                            String.format("Permission with name %s already exists", cate.getName())
                    );
                });

        permission.setName(request.getName());
        return permissionRepository.save(permission);
    }

}
