package org.farozy.service.jwt;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.utility.BaseUrlUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class JwtEmailService {

    private final BaseUrlUtils baseUrlUtil;
    private final SpringTemplateEngine templateEngine;
    private final UserValidationService userValidationService;

    public void sendTokenToEmail(String email, String token) {
        try {
            User user = userValidationService.validateUser(email);

            String htmlContent = generateVerificationEmailContent(token, user);

            userValidationService.sendEmail(email, htmlContent);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while sending token: " + e.getMessage());
        }
    }

    private String generateVerificationEmailContent(String token, User user) {
        String url = baseUrlUtil.getBaseUrl() + "/api/auth/verify-token/" + token;
        Context context = new Context();
        context.setVariable("url", url);
        context.setVariable("token", token);
        context.setVariable("name", user.getFirstName() + " " + user.getLastName());

        return templateEngine.process("emailVerification", context);
    }

}
