package org.farozy.service.permission;

import org.farozy.dto.PermissionDto;
import org.farozy.entity.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> findAll();

    Permission findById(Long id);

    Permission save(PermissionDto request);

    Permission update(Long id, PermissionDto request);

    void delete(Long id);

}
