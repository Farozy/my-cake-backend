package org.farozy.service.auth;

import org.farozy.dto.LoginDto;
import org.farozy.dto.RegistrationDto;
import org.farozy.entity.Registration;
import org.farozy.entity.User;

public interface AuthenticatedService {

    User processUserRegistration(RegistrationDto registrationDto);

    User authenticateUser(LoginDto request);

    Registration checkRegistrationByUserId(Long userId);

    void processSignOut(String token);

}
