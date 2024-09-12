package org.farozy.payload;

import org.farozy.entity.Role;
import org.farozy.entity.User;

import java.util.List;

public record UserRolesResponse(User user, List<Role> roles) {
}
