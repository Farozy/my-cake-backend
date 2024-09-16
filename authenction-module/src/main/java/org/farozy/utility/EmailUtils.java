package org.farozy.utility;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.Registration;
import org.farozy.repository.UserRepository;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.RegistrationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailUtils {

    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    public User validationEmail(String email) {
        try {
            if (isValidEmail(email)) throw new IllegalArgumentException("Invalid email format");

            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The user with the specified email does not exist"
                    ));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while validating email: " + e.getMessage());
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return !email.matches(emailRegex);
    }

    public String getTokenFromEmail(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for email: " + email));

            Optional<Registration> registration = registrationRepository.findByUserId(user.getId());

            String verificationToken;
            verificationToken = registration.map(Registration::getVerificationToken).orElse(null);

            return verificationToken;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}