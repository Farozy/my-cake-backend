package org.farozy.service.role;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.RoleDto;
import org.farozy.entity.Role;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.RoleRepository;
import org.farozy.utility.RolePermissionUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionUtils rolePermissionUtils;

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<Role> findAll() {
        try {
            return roleRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching roles: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Role findById(Long id) {
        try {
            return rolePermissionUtils.getRoleById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching role by ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserCreate
    public Role save(RoleDto request) {
        try {
            return this.saveRoleUpdateRole(null, request);
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving role: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserUpdate
    public Role update(Long id, RoleDto request) {
        try {
            return this.saveRoleUpdateRole(id, request);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating role: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserDelete
    public void delete(Long id) {
        try {
            rolePermissionUtils.getRoleById(id);

            roleRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting role: " + ex.getMessage());
        }
    }

    private Role saveRoleUpdateRole(Long id, RoleDto request) {
        Role role = (id == null) ? new Role() : rolePermissionUtils.getRoleById(id);

        roleRepository.findByName(request.getName())
                .filter(existingCategory -> !existingCategory.getId().equals(id))
                .ifPresent(cate -> {
                    throw new ResourceAlreadyExistsException(
                            String.format("Role with name %s already exists", cate.getName())
                    );
                });

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        return roleRepository.save((role));
    }

}
