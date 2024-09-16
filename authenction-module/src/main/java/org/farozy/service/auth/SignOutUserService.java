package org.farozy.service.auth;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.farozy.enums.RegistrationStatus;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.RegistrationRepository;
import org.farozy.utility.JwtUtils;
import org.farozy.utility.RegistrationUtils;
import org.farozy.utility.UserUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SignOutUserService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationUtils registrationUtils;
    private final UserUtils userUtils;
    private final JwtUtils jwtUtils;

    @Transactional
    public void executeSignOut(String token) {
        try {
            String email = jwtUtils.getEmailFromToken(token);

            User user = userUtils.getUserByEmail(email);

            Registration registration = checkUserRegistration(user);

            registrationRepository.save(registration);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while logout user: " + ex.getMessage());
        }
    }

    private Registration checkUserRegistration(User user) {
        Registration registration = registrationUtils.getUserRegistrationByUserId(user.getId());

        if (!RegistrationStatus.PENDING.equals(registration.getStatus())) {
            registration.setStatus(RegistrationStatus.PENDING);

            return registration;
        } else {
            throw new IllegalArgumentException("Cannot log out an unverified registration");
        }
    }

}
