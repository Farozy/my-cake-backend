package org.farozy.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.farozy.enums.RegistrationStatus;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.RegistrationRepository;
import org.farozy.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email
                ));

        return UserPrincipal.create(user);
    }

    @Transactional
    public boolean validateRegistration(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The user with the specified ID does not exist"
                    ));

            Optional<Registration> register = registrationRepository.findByUserId(user.getId());

            if (register.isPresent()) {
                Registration foundRegister = register.get();

                return foundRegister.getVerificationToken() != null && foundRegister.getStatus() == RegistrationStatus.VERIFIED;
            }

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        return false;
    }

}
