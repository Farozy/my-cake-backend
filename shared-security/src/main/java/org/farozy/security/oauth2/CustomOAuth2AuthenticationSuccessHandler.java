package org.farozy.security.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.farozy.config.SecurityConfigProperties;
import org.farozy.enums.UserRoleType;
import org.farozy.utility.CookieUtils;
import org.farozy.utility.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private final SecurityConfigProperties appProperties;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            System.out.println("Response has already been committed. Unable to redirect to {}" + targetUrl);
        }

        response.sendRedirect(targetUrl);
    }

    private String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws BadRequestException {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry, we've got an Unauthorized redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElseGet(() -> determineUrlBasedOnRole(authentication));
        String token = jwtUtils.generateToken(String.valueOf(authentication));

        UriComponentsBuilder uriComponentsBuilder;
        try {
            uriComponentsBuilder = UriComponentsBuilder.fromUriString(targetUrl);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid redirect URI: " + targetUrl, e);
        }

        response.setStatus(HttpServletResponse.SC_OK);

        return uriComponentsBuilder
                .queryParam("token", token)
                .build()
                .toString();
    }

    private String determineUrlBasedOnRole(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority().replace("ROLE_", "");

            UserRoleType userRoleType = UserRoleType.valueOf(role);

            return switch (userRoleType) {
                case SUPER_ADMIN, ADMIN -> "/dashboard";
                default -> "/home";
            };
        }
        return "/login?error";
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);

                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
