package org.farozy.service.otp;

public interface OtpService {

    String generateOtp();

    void sendOtp(String otp, String userPhoneNumber);

    boolean validateOtp(String phoneNumber, String otp);

}
