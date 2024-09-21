package org.farozy.service.jwt;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final JavaMailSender mailSender;

    public void sendEmail(String email, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Email Verification");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

}
