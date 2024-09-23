package org.farozy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpDto {

    @NotNull(message = "OTP is required")
    private String otp;

    @NotNull(message = "Email or WhatsApp is required")
    @Pattern(
            regexp = "^(\\+62[0-9]{8,13}|0[0-9]{8,13}|[\\w.+\\-]+@\\w+\\.\\w+)$",
            message = "Invalid format for email or WhatsApp number"
    )
    private String emailOrWhatsapp;

}
