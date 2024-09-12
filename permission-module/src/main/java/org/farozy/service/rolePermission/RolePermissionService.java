package org.farozy.service.rolePermission;

import org.farozy.dto.RolePermissionDto;
import org.farozy.entity.RolePermission;
import org.farozy.payload.PermissionRoleResponse;
import org.farozy.payload.RolePermissionResponse;

import java.util.List;

public interface RolePermissionService {

    List<RolePermission> findAll();

    RolePermission findById(Long id);

    PermissionRoleResponse findRoleByPermissionId(Long id);

    RolePermissionResponse findPermissionByRoleId(Long id);

    RolePermission save(RolePermissionDto request);

    RolePermission update(Long id, RolePermissionDto request);

    void delete(Long id);

}