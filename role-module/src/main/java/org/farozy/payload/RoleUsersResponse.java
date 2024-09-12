package org.farozy.payload;

import org.farozy.entity.Role;
import org.farozy.entity.User;

import java.util.List;

public record RoleUsersResponse (Role role, List<User> users) {
}
