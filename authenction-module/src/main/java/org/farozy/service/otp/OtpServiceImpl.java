package org.farozy.service.otp;

import com.twilio.rest.api.v2010.account.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.OtpDto;
import org.farozy.entity.Otp;
import org.farozy.entity.OtpSendLog;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.OtpRepository;
import org.farozy.repository.OtpSendLogRepository;
import org.farozy.repository.UserRepository;
import org.farozy.service.jwt.UserValidationService;
import org.farozy.utility.EmailUtils;
import org.farozy.utility.TwilioProperties;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.twilio.Twilio;

@Component
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final Map<String, OtpRecord> otpStorage = new HashMap<>();
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final SpringTemplateEngine templateEngine;
    private final UserValidationService userValidationService;
    private final OtpSendLogRepository otpSendLogRepository;
    private final EmailUtils emailUtils;
    private static final int MAX_ATTEMPTS = 10;
    private final TwilioProperties twilioProperties;

    private record OtpRecord(String otp, LocalDateTime creationTime) {
    }

    @Override
    @Transactional
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

        return otp;
    }

    private String generateOtp() {
        int otpNumber = ThreadLocalRandom.current().nextInt(0, 1000000);
        return String.format("%06d", otpNumber);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void sendOtpToEmail(String otp, String email) {
        try {
            User user = emailUtils.validationEmail(email);

            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("name", user.getFirstName() + " " + user.getLastName());

            String htmlContent = templateEngine.process("otpVerification", context);

            userValidationService.sendEmail(email, htmlContent);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void sendOtptoWhatsapp(String otp, String whatsappNumber) {
        Twilio.init(twilioProperties.getAccountSid(), twilioProperties.getAuthToken());
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:+6281233663553"),
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                "OTP " + otp
        ).create();

        System.out.println(message.getSid());
    }

    @Override
    @Transactional
    public void countSendOtp(String email, String whatsappNumber) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        OtpSendLog log;
        int optTotal = 3;

        if (email != null && !email.isEmpty()) {
            log = otpSendLogRepository.findByEmailAndLastSentBetween(email, startOfDay, endOfDay)
                    .orElse(new OtpSendLog(email, null, 0, LocalDateTime.now()));
            log.setEmail(email);
        } else if (whatsappNumber != null && !whatsappNumber.isEmpty()) {
            log = otpSendLogRepository.findByWhatsappNumberAndLastSentBetween(whatsappNumber, startOfDay, endOfDay)
                    .orElse(new OtpSendLog(null, whatsappNumber, 0, LocalDateTime.now()));
            log.setWhatsappNumber(whatsappNumber);
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

    @Override
    @Transactional
    public Boolean verifyOtp(OtpDto request) {
        try {
            boolean exists = otpRepository.existsByOtpCode(Integer.valueOf(request.getOtp()));

            if (!exists) {
                throw new IllegalArgumentException("Invalid OTP code");
            }

            Otp otp = otpRepository.findByOtpCode(Integer.valueOf(request.getOtp()))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid OTP code."));

            if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("OTP has expired.");
            }

            otp.setUsed(true);
            otpRepository.save(otp);

            return true;
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
