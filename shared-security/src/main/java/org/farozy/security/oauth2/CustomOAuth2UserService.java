package org.farozy.security.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.farozy.entity.Registration;
import org.farozy.entity.Role;
import org.farozy.entity.User;
import org.farozy.enums.AuthProvider;
import org.farozy.enums.RegistrationStatus;
import org.farozy.exception.OAuth2AuthenticationProcessingException;
import org.farozy.helper.FileUploadHelper;
import org.farozy.repository.RegistrationRepository;
import org.farozy.repository.RoleRepository;
import org.farozy.repository.UserRepository;
import org.farozy.security.UserPrincipal;
import org.farozy.security.oauth2.user.OAuth2UserInfo;
import org.farozy.security.oauth2.user.OAuth2UserInfoFactory;
import org.farozy.utility.JwtUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String DEFAULT_ROLE_NAME = "USER";
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User)
            throws OAuth2AuthenticationProcessingException, MalformedURLException, URISyntaxException {

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes()
        );

        String email;

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            String foundEmail = fetchEmailFromGitHub(oAuth2UserRequest.getAccessToken().getTokenValue());

            if (!StringUtils.hasText(foundEmail)) {
                throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
            }
            email = foundEmail;
        } else {
            email = oAuth2UserInfo.getEmail();
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            updateRegistration(user, oAuth2UserRequest);
            user = updateExisingUser(user, oAuth2UserInfo, oAuth2UserRequest);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo, email);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo, String email) throws MalformedURLException, URISyntaxException {
        if (oAuth2UserInfo == null) throw new IllegalArgumentException("OAuth2UserInfo cannot be null");

        if (email == null) throw new IllegalArgumentException("Email connot be null");

        User user = new User();

        String fullName = oAuth2UserInfo.getName();
        String firstName = "";
        String lastName = "";

        if (fullName != null && !fullName.isEmpty()) {
            String[] nameParts = fullName.split(" ", 2);
            firstName = nameParts[0];
            if (nameParts.length > 1) {
                lastName = nameParts[1];
            }
        }

        user.setUsername(oAuth2UserInfo.getName());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        String randomUUID = UUID.randomUUID().toString();
        String imageName = randomUUID + ".webp";
        String imagePath = FileUploadHelper.saveImageLocally(oAuth2UserInfo.getImageUrl(), imageName, "thumbnails");

        user.setImage(imagePath);

        Set<Role> roles = new HashSet<>();
        roles.add(getDefaultRole());
        user.setRoles(roles);

        User newUser = userRepository.save(user);

        this.updateRegistration(newUser, oAuth2UserRequest);

        return newUser;
    }

    private User updateExisingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest) {
        if (oAuth2UserInfo == null) throw new IllegalArgumentException("OAuth2UserInfo cannot be null");

        existingUser.setFirstName(oAuth2UserInfo.getName());
        existingUser.setImage(oAuth2UserInfo.getImageUrl());

        User updatedUser = userRepository.save(existingUser);

        this.updateRegistration(updatedUser, oAuth2UserRequest);

        return updatedUser;
    }

    private Role getDefaultRole() {
        return roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new IllegalStateException("Default role not found: " + DEFAULT_ROLE_NAME));
    }

    public void updateRegistration(User user, OAuth2UserRequest oAuth2UserRequest) {
        Registration registration = registrationRepository.findByUserId(user.getId())
                .orElseGet(Registration::new);

        String generateToken = jwtUtils.generateToken(user.getEmail());

        registration.setVerificationToken(generateToken);
        registration.setStatus(RegistrationStatus.VERIFIED);
        registration.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        registration.setUser(user);

        registrationRepository.save(registration);
    }

    private String fetchEmailFromGitHub(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            for (JsonNode emailNode : root) {
                if (emailNode.get("primary").asBoolean() && emailNode.get("verified").asBoolean()) {
                    return emailNode.get("email").asText();
                }
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
