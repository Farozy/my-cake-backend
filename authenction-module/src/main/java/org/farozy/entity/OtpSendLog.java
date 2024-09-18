package org.farozy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "otp_send_logs")
public class OtpSendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "whatsapp_number", length = 10)
    private Integer whatsappNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "otp_count")
    private int otpCount;

    @Column(name = "last_sent")
    private LocalDateTime lastSent;

    public OtpSendLog(String email, String whatsappNumber, int otpCount, LocalDateTime lastSent) {
        this.email = email;
        this.whatsappNumber = parseWhatsappNumber(whatsappNumber);
        this.otpCount = otpCount;
        this.lastSent = lastSent;
    }

    public OtpSendLog() {}

    private Integer parseWhatsappNumber(String whatsappNumber) {
        try {
            return whatsappNumber != null ? Integer.valueOf(whatsappNumber) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }


}