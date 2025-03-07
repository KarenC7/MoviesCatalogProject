package com.catalog.movies.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.util.Date;

@Component
public class JwtTokenProvider {

    // Secret key -> change to properties file
    private final String jwtSecret = "SecretKeyExample";

    // Expiration time (7 days for testing purposes)
    private final long jwtExpirationInMs = 604800000;

    /**
     * Generate a token JWT for auth user.
     *
     * @param authentication Auth Object contains user details
     * @return Token JWT as string
     */
    public String generateToken(Authentication authentication) {

        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Extract username (email) from token JWT.
     *
     * @param token Token JWT
     * @return Username extracting from token
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Validate token JWT.
     *
     * @param authToken Token JWT to validate
     * @return true id it is a valid token, false if it is not
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());  // Add logger instead sout
        }
        return false;
    }
}