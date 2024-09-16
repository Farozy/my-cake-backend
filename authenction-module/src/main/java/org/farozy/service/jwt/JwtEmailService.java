package org.farozy.service.jwt;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.utility.BaseUrlUtils;
import org.farozy.utility.EmailUtils;
import org.farozy.utility.UserUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class JwtEmailService {

    private final BaseUrlUtils baseUrlUtil;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserUtils userUtils;

    public void sendTokenToEmail(String email, String token) {
        try {
            if (EmailUtils.isValidEmail(email)) throw new IllegalArgumentException("Invalid email format");

            User user = userUtils.getUserByEmail(email);

            String htmlContent = generateVerificationEmailContent(token, user);

            sendEmail(email, htmlContent);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while sending token: " + e.getMessage());
        }
    }

    public String generateVerificationEmailContent(String token, User user) {
        String url = baseUrlUtil.getBaseUrl() + "/api/auth/verify-token/" + token;
        Context context = new Context();
        context.setVariable("url", url);
        context.setVariable("token", token);
        context.setVariable("name", user.getFirstName() + " " + user.getLastName());

        return templateEngine.process("emailVerification", context);
    }

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
