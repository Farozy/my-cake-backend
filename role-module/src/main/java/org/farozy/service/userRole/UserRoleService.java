package org.farozy.service.userRole;

import org.farozy.dto.UserRoleDto;
import org.farozy.entity.UserRole;
import org.farozy.payload.RoleUsersResponse;
import org.farozy.payload.UserRolesResponse;

import java.util.List;

public interface UserRoleService {

    List<UserRole> getAllUserRoles();

    UserRole findById(Long id);

    UserRolesResponse findRoleByUserId(Long id);

    RoleUsersResponse findUserByRoleId(Long id);

    UserRole addRoleToUser(UserRoleDto request);

    UserRole updateUserRoleAssignment(Long id, UserRoleDto request);

    void unassignRoleFromUser(Long id);

}
