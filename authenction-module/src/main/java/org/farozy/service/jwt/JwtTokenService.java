package org.farozy.service.jwt;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.farozy.enums.RegistrationStatus;
import org.farozy.exception.InvalidTokenException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.RegistrationRepository;
import io.jsonwebtoken.security.SignatureException;
import org.farozy.utility.JwtUtils;
import org.farozy.utility.RegistrationUtils;
import org.farozy.utility.UserUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private final RegistrationRepository registrationRepository;
    private final UserUtils userUtils;
    private final RegistrationUtils registrationUtils;
    private final JwtUtils jwtUtils;

    public void validationToken(String token, String message) {
        try {
            String email = jwtUtils.getEmailFromToken(token);

            User user = userUtils.getUserByEmail(email);

            if (Objects.equals(message, "confirm")) updateRegistrationUser(token, user);

        } catch (SignatureException e) {
            throw new SignatureException(e.getMessage());
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException(e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateRegistrationUser(String token, User user) {
        Registration registration = registrationUtils.getUserRegistrationByUserId(user.getId());

        registration.setVerificationToken(token);
        registration.setStatus(RegistrationStatus.VERIFIED);
        registrationRepository.save(registration);
    }

    public String renewToken(String token) {
        try {
            jwtUtils.validateToken(token);
            String email = jwtUtils.getEmailFromToken(token);

            User user = userUtils.getUserByEmail(email);

            return checkUserRegistration(user, token, email);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Invalid refresh token: " + e.getMessage());
        }
    }

    private String checkUserRegistration(User user, String token, String email) {
        Registration foundRegistrationById = registrationUtils.getUserRegistrationByUserId(user.getId());

        if (!foundRegistrationById.getVerificationToken().equals(token))
            throw new InvalidTokenException("The provided verification token is invalid");

        String newToken;
        if (jwtUtils.isTokenExpired(token)) {
            newToken = jwtUtils.generateToken(email);
        } else {
            newToken = foundRegistrationById.getVerificationToken();
        }

        foundRegistrationById.setVerificationToken(newToken);
        foundRegistrationById.setStatus(RegistrationStatus.VERIFIED);

        registrationRepository.save(foundRegistrationById);

        return newToken;
    }

}
