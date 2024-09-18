package org.farozy.service.otp;

public interface OtpService {

    String generateOtpForUser(String emailOrWhatsapp);

    void sendOtp(String otp, String userPhoneNumber);

    boolean validateOtp(String identifier, String otp);

    void sendOtpToEmail(String otp, String email);

    void countSendOtp(String email, String whatsappNumber);

}
