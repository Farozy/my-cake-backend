package org.farozy.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityConfigProperties {

    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    public static final class OAuth2 {
        private final List<String> authorizedRedirectUris = new ArrayList<>();
    }

}
