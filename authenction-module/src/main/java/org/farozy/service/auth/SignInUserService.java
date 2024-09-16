package org.farozy.service.auth;

import lombok.RequiredArgsConstructor;
import org.farozy.repository.UserRepository;
import org.farozy.dto.LoginDto;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.farozy.enums.RegistrationStatus;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.RegistrationRepository;
import org.farozy.utility.JwtUtils;
import org.farozy.utility.RegistrationUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignInUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationRepository registrationRepository;
    private final RegistrationUtils registrationUtils;
    private final JwtUtils jwtUtils;

    @Transactional
    public Registration getRegistrationByUserId(Long userId) {
        try {
            Registration foundRegistration = registrationUtils.getUserRegistrationByUserId(userId);

            if (foundRegistration.getVerificationToken() != null) {
                boolean isExpired = jwtUtils.isTokenExpired(foundRegistration.getVerificationToken());

                checkExpiredToken(foundRegistration, isExpired);

                return foundRegistration;
            }

            return foundRegistration;
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Failed to retrieve login: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while retrieving login: " + ex.getMessage());
        }
    }

    private void checkExpiredToken(Registration foundRegistration, boolean isExpired) {
        if (isExpired) {
            foundRegistration.setStatus(RegistrationStatus.EXPIRED);
            registrationRepository.save(foundRegistration);
        } else {
            foundRegistration.setStatus(RegistrationStatus.VERIFIED);
            registrationRepository.save(foundRegistration);
        }
    }

    public User authorizeUser(LoginDto request) {
        try {
            String login = request.getLogin();
            String password = request.getPassword();

            User user = userRepository.findByUsername(login)
                    .or(() -> userRepository.findByEmail(login))
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The user with the specified username or email does not exist"
                    ));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Login attempt failed due to incorrect password.");
            }

            return user;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + " Please try again");
        }
    }

}
