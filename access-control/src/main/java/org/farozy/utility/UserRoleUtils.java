package org.farozy.utility;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.UserRole;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.UserRoleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleUtils {

    private final UserRoleRepository userRoleRepository;

    public UserRole getUserRoleById(Long id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user role with the specified ID does not exist"
                ));
    }
}
