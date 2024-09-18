package org.farozy.service.auth;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.RegistrationDto;
import org.farozy.entity.Otp;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.farozy.enums.AuthProvider;
import org.farozy.enums.RegistrationStatus;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.repository.OtpRepository;
import org.farozy.repository.RegistrationRepository;
import org.farozy.repository.UserRepository;
import org.farozy.security.CustomAuditEventHandler;
import org.farozy.security.CustomAuditEventRepository;
import org.farozy.service.otp.OtpService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegistrationUserService {

    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public User registerUser(RegistrationDto request) {
        try {
            String emailOrWhatappNumber = request.getEmail() != null ? request.getEmail() : request.getWhatsAppNumber();

            userRepository.findByEmail(emailOrWhatappNumber)
                    .ifPresent(user -> {
                        throw new ResourceAlreadyExistsException(
                                "User with email or whatsapp already exists"
                        );
                    });

            User newUser = saveNewUser(request);

            saveRegistration(newUser);

            String emailOrWhatsapp = newUser.getEmail() != null ? newUser.getEmail() : newUser.getWhatsappNumber();

            String generateOtp = otpService.generateOtpForUser(emailOrWhatsapp);

            saveOtp(newUser, generateOtp);

            otpService.sendOtpToEmail(generateOtp, newUser.getEmail());

            otpService.countSendOtp(emailOrWhatsapp, emailOrWhatsapp);

            eventPublisher.publishEvent(new CustomAuditEventHandler(this, newUser));

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
        Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(request.getWhatsAppNumber()).ifPresent(user::setWhatsappNumber);

        return userRepository.save(user);
    }

    private void saveOtp(User newUser, String generateOtp) {
        Otp otp = new Otp();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        otp.setUser(newUser);
        otp.setOtpCode(Integer.valueOf(generateOtp));
        otp.setExpiresAt(expiresAt);

        otpRepository.save(otp);
    }
}
