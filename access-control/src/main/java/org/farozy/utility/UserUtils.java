package org.farozy.utility;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.User;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with the specified ID does not exist"
                ));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with the specified email does not exist"
                ));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with the specified username does not exist"
                ));
    }

    public void validateUsernameUniqueness(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new ResourceAlreadyExistsException(
                            String.format("User with username '%s' already exists", username)
                    );
                });
    }

    public void validateEmailUniqueness(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new ResourceAlreadyExistsException(
                            String.format("User with email '%s' already exists", email)
                    );
                });
    }

}
