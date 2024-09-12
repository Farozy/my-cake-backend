package org.farozy.service.role;

import org.farozy.dto.RoleDto;
import org.farozy.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    Role findById(Long id);

    Role save(RoleDto request);

    Role update(Long id, RoleDto request);

    void delete(Long id);

}
