package org.farozy.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.farozy.dto.OtpDto;
import org.farozy.dto.RegistrationDto;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.otp.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@AllArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Object>> resendOtp(@RequestBody @Valid RegistrationDto request) {
        String otp;
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            otp = String.valueOf(otpService.generateOtpForUser(request.getEmail()));
            otpService.countSendOtp(request.getEmail(), null);
            otpService.sendOtpToEmail(otp, request.getEmail());

            return ResponseHelper.buildResponseToken(HttpStatus.OK, "OTP sent to Email");
        } else if (request.getWhatsAppNumber() != null && !request.getWhatsAppNumber().isEmpty()) {
            otp = String.valueOf(otpService.generateOtpForUser(request.getWhatsAppNumber()));
            otpService.countSendOtp(null, request.getWhatsAppNumber());
            otpService.sendOtptoWhatsapp(otp, request.getWhatsAppNumber());

            return ResponseHelper.buildResponseToken(HttpStatus.OK, "OTP sent to WhatsApp number");
        } else {
            return ResponseHelper.buildResponseToken(
                    HttpStatus.BAD_REQUEST,
                    "Either email or whatsAppNumber must be provided"
            );
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@RequestBody @Valid OtpDto request) {
        boolean isValidOtp = otpService.verifyOtp(request);

        if (isValidOtp) {
            return ResponseHelper.buildResponseToken(
                    HttpStatus.OK,
                    "OTP is valid"
            );
        } else {
            return ResponseHelper.buildResponseToken(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid OTP"
            );
        }
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
