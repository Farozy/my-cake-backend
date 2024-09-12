package org.farozy.repository;

import org.farozy.entity.Role;
import org.farozy.entity.User;
import org.farozy.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    List<UserRole> findRoleByUser(User user);

    List<UserRole> findUserByRole(Role role);

}