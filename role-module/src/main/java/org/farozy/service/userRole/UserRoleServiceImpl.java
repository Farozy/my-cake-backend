package org.farozy.service.userRole;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.UserRoleDto;
import org.farozy.entity.Role;
import org.farozy.entity.User;
import org.farozy.entity.UserRole;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.payload.RoleUsersResponse;
import org.farozy.payload.UserRolesResponse;
import org.farozy.repository.RoleRepository;
import org.farozy.repository.UserRepository;
import org.farozy.repository.UserRoleRepository;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<UserRole> getAllUserRoles() {
        try {
            return userRoleRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching user role: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public UserRole findById(Long id) {
        try {
            return getUserRoleById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching user role by ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public UserRolesResponse findRoleByUserId(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The user with the specified ID does not exist"
                    ));

            List<Role> roles = userRoleRepository.findRoleByUser(user)
                    .stream().map(UserRole::getRole)
                    .collect(Collectors.toList());

            return new UserRolesResponse(user, roles);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching roles by user ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public RoleUsersResponse findUserByRoleId(Long id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The role with the specified ID does not exist"
                    ));

            List<User> users = userRoleRepository.findUserByRole(role)
                    .stream().map(UserRole::getUser)
                    .collect(Collectors.toList());

            return new RoleUsersResponse(role, users);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching users by role ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserCreate
    public UserRole addRoleToUser(UserRoleDto request) {
        try {
            User user = getUserById(request.getUserId());
            Role role = getRoleById(request.getRoleId());

            processCheckingUserRole(user.getId(), role.getId());

            return saveOrUpdateUserRole(null, user, role);
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while adding role to user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserUpdate
    public UserRole updateUserRoleAssignment(Long id, UserRoleDto request) {
        try {
            getUserRoleById(id);

            User user = getUserById(request.getUserId());
            Role role = getRoleById(request.getRoleId());

            processCheckingUserRole(user.getId(), role.getId());

            return saveOrUpdateUserRole(id, user, role);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating user role: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserDelete
    public void unassignRoleFromUser(Long id) {
        try {
            getUserRoleById(id);

            userRoleRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting user role: " + ex.getMessage());
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with the specified ID does not exist"
                ));
    }

    private Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The role with the specified ID does not exist"
                ));
    }

    private UserRole getUserRoleById(Long id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user role with the specified ID does not exist"
                ));
    }

    private void processCheckingUserRole(Long userId, Long roleId) {
        userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .ifPresent(userRole -> {
                    throw new ResourceAlreadyExistsException(
                            String.format("The user with ID %s already has the role with ID %s ",
                                    userRole.getUser().getId(), userRole.getRole().getId()
                            )
                    );
                });
    }

    private UserRole saveOrUpdateUserRole(Long id, User user, Role role) {
        UserRole userRole = (id == null) ? new UserRole() : getUserRoleById(id);

        userRole.setUser(user);
        userRole.setRole(role);
        return userRoleRepository.save(userRole);
    }

}

