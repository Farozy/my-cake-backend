package org.farozy.service.otp;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.farozy.config.TwilioConfig;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final TwilioConfig twilioConfig;

    private final SecureRandom random = new SecureRandom();
    private final Map<String, OtpRecord> otpStorage = new HashMap<>();

    @Override
    public String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }

    @Override
    public void sendOtp(String otp, String userPhoneNumber) {
        String messageBody = "Your OTP code is: " + otp;

        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + userPhoneNumber),
                new PhoneNumber("whatsapp:" + twilioConfig.getFromPhoneNumber()),
                messageBody
        ).create();

        System.out.println("OTP sent with SID: " + message.getSid());
    }

    @Override
    public boolean validateOtp(String phoneNumber, String otp) {
        OtpRecord record = otpStorage.get(phoneNumber);
        if (record != null && record.otp().equals(otp)) {
            // Check if OTP has expired (e.g., 5 minutes validity)
            if (LocalDateTime.now().isBefore(record.creationTime().plusMinutes(5))) {
                otpStorage.remove(phoneNumber); // Remove OTP after successful validation
                return true;
            } else {
                otpStorage.remove(phoneNumber); // Remove expired OTP
            }
        }
        return false;
    }

    private record OtpRecord(String otp, LocalDateTime creationTime) {
    }

}
