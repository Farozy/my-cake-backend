package org.farozy.service.jwt;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.UserRepository;
import org.farozy.utility.EmailUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    public void sendEmail(String email, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Email Verification");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public User validateUser(String email) {
        if (EmailUtils.isValidEmail(email)) throw new IllegalArgumentException("Invalid email format");

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with the specified email does not exist"));
    }

}
