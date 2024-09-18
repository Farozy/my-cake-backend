package org.farozy.service.otp;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.farozy.config.TwilioConfig;
import org.farozy.entity.Otp;
import org.farozy.entity.OtpSendLog;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.OtpRepository;
import org.farozy.repository.OtpSendLogRepository;
import org.farozy.repository.UserRepository;
import org.farozy.service.jwt.UserValidationService;
import org.farozy.utility.EmailUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final Map<String, OtpRecord> otpStorage = new HashMap<>();
    private final TwilioConfig twilioConfig;
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final SpringTemplateEngine templateEngine;
    private final UserValidationService userValidationService;
    private final OtpSendLogRepository otpSendLogRepository;
    private final EmailUtils emailUtils;
    private static final int MAX_ATTEMPTS = 10;

    private record OtpRecord(String otp, LocalDateTime creationTime) {
    }

    @Override
    public String generateOtpForUser(String emailOrWhatsapp) {
        try {
            User user = userRepository.findByEmail(emailOrWhatsapp)
                    .orElseGet(() -> userRepository.findByWhatsappNumber(emailOrWhatsapp)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "The user with the specified email or WhatsApp number does not exist")));

            return generateUniqueOtp(user);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private String generateUniqueOtp(User user) {
        List<Otp> existingOtps = otpRepository.findByUser(user);

        Otp validOtp = existingOtps.stream()
                .filter(otp -> otp.getExpiresAt().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElse(null);

        if (validOtp != null) return validOtp.getOtpCode().toString();

        int attempts = 0;
        String otp;

        do {
            otp = generateOtp();
            attempts++;
        } while (otpRepository.existsByOtpCode(Integer.valueOf(otp)) && attempts < MAX_ATTEMPTS);

        if (attempts == MAX_ATTEMPTS) {
            throw new RuntimeException("Unable to generate a unique OTP.");
        }

        Otp otpEntity = new Otp();
        otpEntity.setUser(user);
        otpEntity.setOtpCode(Integer.valueOf(otp));
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otpEntity);

        return otp;
    }

    private String generateOtp() {
        int otpNumber = ThreadLocalRandom.current().nextInt(0, 1000000);
        return String.format("%06d", otpNumber);
    }

    @Override
    public void sendOtp(String otp, String userPhoneNumber) {
        String messageBody = "Your OTP code is: " + otp;

        Message.creator(
                new PhoneNumber("whatsapp:" + userPhoneNumber),
                new PhoneNumber("whatsapp:" + twilioConfig.getFromPhoneNumber()),
                messageBody
        ).create();
    }

    @Override
    public boolean validateOtp(String identifier, String otp) {
        OtpRecord record = otpStorage.get(identifier);

        if (record != null && record.otp().equals(otp)) {
            if (LocalDateTime.now().isBefore(record.creationTime().plusMinutes(5))) {
                otpStorage.remove(identifier);
                return true;
            } else {
                otpStorage.remove(identifier);
            }
        }
        return false;
    }

    public void sendOtpToEmail(String otp, String email) {
        try {
            User user = emailUtils.validationEmail(email);

            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("name", user.getFirstName() + " " + user.getLastName());

            String htmlContent = templateEngine.process("emailVerification", context);

            userValidationService.sendEmail(email, htmlContent);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void countSendOtp(String email, String whatsappNumber) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        OtpSendLog log;
        int optTotal = 3;

        if (email != null && !email.isEmpty()) {
            log = otpSendLogRepository.findByEmailAndLastSentBetween(email, startOfDay, endOfDay)
                    .orElse(new OtpSendLog(null, email, 0, LocalDateTime.now()));
            log.setEmail(email);
        } else if (whatsappNumber != null && !whatsappNumber.isEmpty()) {
            log = otpSendLogRepository.findByWhatsappNumberAndLastSentBetween(Integer.valueOf(whatsappNumber), startOfDay, endOfDay)
                    .orElse(new OtpSendLog(whatsappNumber, null, 0, LocalDateTime.now()));
            log.setWhatsappNumber(Integer.valueOf(whatsappNumber));
        } else {
            throw new IllegalArgumentException("Email atau WhatsApp number harus diberikan");
        }

        if (log.getOtpCount() >= optTotal) {
            throw new RuntimeException("Anda telah mencapai batas maksimum pengiriman OTP untuk hari ini");
        }

        log.setOtpCount(log.getOtpCount() + 1);
        log.setLastSent(LocalDateTime.now());
        otpSendLogRepository.save(log);
    }

}
