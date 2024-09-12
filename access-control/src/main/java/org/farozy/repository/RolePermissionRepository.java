package org.farozy.repository;

import org.farozy.entity.Permission;
import org.farozy.entity.Role;
import org.farozy.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    List<RolePermission> findRoleByPermission(Permission permission);

    List<RolePermission> findPermissionByRole(Role role);

}
