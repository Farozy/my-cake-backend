package org.farozy.controller;

import lombok.RequiredArgsConstructor;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.auth.AuthenticatedService;
import org.farozy.service.jwt.JwtEmailService;
import org.farozy.service.jwt.JwtTokenService;
import org.farozy.utility.EmailUtils;
import org.farozy.utility.TokenInfoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class JwtTokenController {

    private final JwtTokenService customTokenService;
    private final EmailUtils emailUtils;
    private final JwtEmailService jwtEmailService;
    private final TokenInfoUtils tokenInfoUtils;
    private final AuthenticatedService authenticatedService;

    @PostMapping("/generate-token")
    public ResponseEntity<ApiResponse<Object>> generateTokenToEmail(@RequestParam String email) {
        User user = emailUtils.validationEmail(email);
        String message = "Generate token successfully";

        Registration registrationById = authenticatedService.checkRegistrationByUserId(user.getId());

        if (registrationById.getVerificationToken() == null) {
            return ResponseHelper.buildResponseData(HttpStatus.BAD_REQUEST, "User not verified", null);
        }

        TokenInfoUtils.TokenInfo tokenInfo = tokenInfoUtils.createTokenInfo(user.getEmail(), null);

        return ResponseHelper.buildResponseToken(HttpStatus.CREATED, message, user, tokenInfo);
    }

    @PostMapping("/send-token")
    public ResponseEntity<ApiResponse<Void>> sendTokenToEmail(@RequestParam String email) {
        User user = emailUtils.validationEmail(email);
        String message = "Token sent successfully";

        TokenInfoUtils.TokenInfo tokenInfo = tokenInfoUtils.createTokenInfo(user.getEmail(), null);

        jwtEmailService.sendTokenToEmail(user.getEmail(), tokenInfo.accessToken());

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

    @GetMapping("/verify-token/{token}")
    public ResponseEntity<ApiResponse<User>> verifyToken(@PathVariable String token) {
        customTokenService.validationToken(token, "confirm");
        String message = "Verify token succesfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Object>> refreshToken(@RequestParam String token) {
        String newToken = customTokenService.renewToken(token);
        String message = "New access token generated successfully";

        TokenInfoUtils.TokenInfo tokenInfo = tokenInfoUtils.createTokenInfo(null, newToken);

        return ResponseHelper.buildResponseToken(HttpStatus.OK, message, null, tokenInfo);
    }

}
