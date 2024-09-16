package org.farozy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.LoginDto;
import org.farozy.dto.RegistrationDto;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.farozy.enums.RegistrationStatus;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.auth.AuthenticatedService;
import org.farozy.service.jwt.JwtTokenService;
import org.farozy.utility.TokenInfoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticatedController {

    private final AuthenticatedService authenticatedService;
    private final JwtTokenService jwtTokenService;
    private final TokenInfoUtils tokenInfoUtils;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody @Valid RegistrationDto request) {
        User newUser = authenticatedService.processUserRegistration(request);
        String message = "User registered successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody @Valid LoginDto request) {
        User user = authenticatedService.authenticateUser(request);
        String message = "Successfully logged in";
        Registration registrationById = authenticatedService.checkRegistrationByUserId(user.getId());

        if (registrationById.getVerificationToken() == null) {
            return ResponseHelper.buildResponseData(HttpStatus.BAD_REQUEST, "User not verified", null);
        }

        if (RegistrationStatus.EXPIRED.equals(registrationById.getStatus())) {
            return ResponseHelper.buildResponseData(HttpStatus.FORBIDDEN, "Token was expired", null);
        }

        TokenInfoUtils.TokenInfo tokenInfo = tokenInfoUtils.createTokenInfo(user.getEmail(), null);

        return ResponseHelper.buildResponseToken(HttpStatus.OK, message, user, tokenInfo);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<User>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        jwtTokenService.validationToken(token, null);
        String message = "Logout completed successfully";
        authenticatedService.processSignOut(token);
        SecurityContextHolder.clearContext();

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

}
