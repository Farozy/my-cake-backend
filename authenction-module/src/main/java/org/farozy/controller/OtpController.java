package org.farozy.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.farozy.dto.RegistrationDto;
import org.farozy.service.otp.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@AllArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody @Valid RegistrationDto request) {
        String otp = otpService.generateOtp();
        otpService.sendOtp(otp, request.getWhatsAppNumber());
        return ResponseEntity.ok("OTP sent to: " + request.getWhatsAppNumber());
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(phoneNumber, otp);

        if (isValid) {
            return ResponseEntity.ok("OTP is valid.");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP.");
        }
    }

}
