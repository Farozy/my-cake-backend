package org.farozy.service.auth;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.RegistrationDto;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.farozy.enums.AuthProvider;
import org.farozy.enums.RegistrationStatus;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.repository.RegistrationRepository;
import org.farozy.repository.UserRepository;
import org.farozy.service.jwt.JwtEmailService;
import org.farozy.utility.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RegistrationUserService {

    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final JwtEmailService jwtEmailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public User registerUser(RegistrationDto request) {
        try {
            User newUser = saveNewUser(request);

            saveRegistration(newUser);

            String token = jwtUtils.generateToken(newUser.getEmail());
            jwtEmailService.sendTokenToEmail(newUser.getEmail(), token);

            return newUser;
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Failed to register user. Please check the provided details and try again: " + ex.getMessage()
            );
        }
    }

    private void saveRegistration(User newUser) {
        Registration registration = new Registration();
        registration.setUser(newUser);
        registration.setStatus(RegistrationStatus.PENDING);
        registration.setProvider(AuthProvider.local);

        registrationRepository.save(registration);
    }

    private User saveNewUser(RegistrationDto request) {
        User user = new User();
        String passwordEncode = passwordEncoder.encode(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncode);

        return userRepository.save(user);
    }
}
