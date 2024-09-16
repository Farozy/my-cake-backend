package org.farozy.utility;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.Registration;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.RegistrationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationUtils {

    private final RegistrationRepository registrationRepository;

    public Registration getUserRegistrationByUserId(Long userId) {
        return registrationRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with the specified id user does not exist"
                ));
    }

}
