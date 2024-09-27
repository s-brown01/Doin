package edu.carroll.doin_backend.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * This is a Utility class to create JWT Tokens, validate them, check expiration, and extract data.
 *
 * <BR>
 * The creation of this class was assisted by ChatGPT
 */


@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour in milliseconds
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Generate a secure random key for HS256
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * Create a new JWT Token for a specific user (identified by the username) that expires in one hour.
     *
     * @param username - the unique username of the user signed in
     * @return the newly generated JWT-Token that identifies the user
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)  // Use the secure key
                .compact();
    }

    /**
     * Validate the token against the username.
     *
     * @param token    - the JWT Token to check and compare against the username
     * @param username - the username to verify against the token
     * @return true if the username matches the JWTToken's stored data and the token is not expired.
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Extract the username from the JWT Token.
     *
     * @param token - the token to extract the username from
     * @return - the username that was stored in the token.
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extract claims from the token.
     *
     * @param token - the JWT token from which claims need to be extracted
     * @return claims - the claims contained in the JWT token
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Use the secure key for validation
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if the token is expired.
     *
     * @param token - the JWT token to check for expiration
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
