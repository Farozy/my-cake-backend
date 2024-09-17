package org.farozy.service.otp;

import org.farozy.entity.Otp;
import org.farozy.entity.User;

public interface OtpService {

    String generateOtpForUser(String emailOrWhatsapp);

    void sendOtp(String otp, String userPhoneNumber);

    boolean validateOtp(String identifier, String otp);

}
