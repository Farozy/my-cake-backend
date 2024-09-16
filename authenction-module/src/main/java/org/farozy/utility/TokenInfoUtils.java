package org.farozy.utility;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenInfoUtils {

    private final JwtUtils jwtUtils;
    private final EmailUtils emailUtils;

    public TokenInfo createTokenInfo(String email, String token) {
        String accessToken;
        String type = "Bearer";

        if (email != null) {
            String userToken = emailUtils.getTokenFromEmail(email);

            if (userToken == null || jwtUtils.isTokenExpired(userToken)) {
                accessToken = jwtUtils.generateToken(email);
            } else {
                accessToken = userToken;
            }
        } else {
            accessToken = token;
        }

        long expirationTime = jwtUtils.getExpirationTime(accessToken);

        return new TokenInfo(accessToken, type, expirationTime);
    }

    public record TokenInfo(String accessToken, String type, long expirationTime) {
    }
}