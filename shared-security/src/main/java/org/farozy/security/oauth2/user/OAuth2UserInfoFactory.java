package org.farozy.security.oauth2.user;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "github" -> new GithubOAuth2UserInfo(attributes);
            case "facebook" -> new FaceboookAuth2UserInfo(attributes);
            default -> throw new UnsupportedOperationException(
                    "Sorry! Login with " + registrationId + " is not supported yet."
            );
        };
    }

}
