package org.farozy.utility;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.farozy.config.JwtConfig;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
@Getter
public class JwtUtils {

    private final SecretKey secretKey;
    private final Long expirationMS;

    public JwtUtils(JwtConfig jwtConfig) {
        this.secretKey = new SecretKeySpec(jwtConfig.getSecret().getBytes(), "HmacSHA256");
        this.expirationMS = jwtConfig.getExpirationMS();
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMS);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public long getExpirationTime(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.getTime() / 1000L;
    }

    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            if (expirationDate == null) {
                throw new IllegalArgumentException("Expiration date is missing from the token.");
            }

            return expirationDate;
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Expired JWT token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims().getSubject();
        } catch (JwtException ex) {
            throw new RuntimeException("Invalid JWT token", ex);
        }
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

}
