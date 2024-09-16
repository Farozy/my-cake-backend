package org.farozy.service.auth;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.LoginDto;
import org.farozy.dto.RegistrationDto;
import org.farozy.entity.Registration;
import org.farozy.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedServiceImpl implements AuthenticatedService{

    private final RegistrationUserService registrationUserService;
    private final SignInUserService signInUserService;
    private final SignOutUserService signOutUserService;

    @Override
    public User processUserRegistration(RegistrationDto request) {
        return registrationUserService.registerUser(request);
    }

    @Override
    public User authenticateUser(LoginDto request) {
        return signInUserService.authorizeUser(request);
    }

    @Override
    public Registration checkRegistrationByUserId(Long userId) {
        return signInUserService.getRegistrationByUserId(userId);
    }

    @Override
    public void processSignOut(String token) {
        signOutUserService.executeSignOut(token);
    }
}
