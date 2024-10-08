package org.farozy.service.otp;

import org.farozy.dto.OtpDto;

public interface OtpService {

    String generateOtpForUser(String emailOrWhatsapp);

    boolean validateOtp(String identifier, String otp);

    void sendOtpToEmail(String otp, String email);

    void sendOtptoWhatsapp(String otp, String whatsappNumber);

    void countSendOtp(String email, String whatsappNumber);

    Boolean verifyOtp(OtpDto request);

}
