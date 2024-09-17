package org.farozy.service.jwt;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class JwtOtpService {

    private final SpringTemplateEngine templateEngine;
    private final UserValidationService userValidationService;

    public void sendOtpToEmail(String otp, String email) {
        try {
            User user = userValidationService.validateUser(email);

            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("name", user.getFirstName() + " " + user.getLastName());

            String htmlContent = templateEngine.process("emailVerification", context);

            userValidationService.sendEmail(email, htmlContent);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while sending otp: " + e.getMessage());
        }
    }

}
