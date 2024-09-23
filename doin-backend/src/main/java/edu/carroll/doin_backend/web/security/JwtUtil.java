package edu.carroll.doin_backend.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * This is a Utility class to create JWT Tokens, validate them, check expiration, and extract data.
 *
 * <BR>
 * The creation of this class was assisted by ChatGPT
 */
@Component
public class JwtUtil {

    private String secretKey = "super_secret";
    // 1 hour in miliseconds: 1 hr * 60 m/hr * 60 sec/min * 1000 ms/sec 
    private final long expirationTime = 1000 * 60 * 60;

    /**
     * Create a new JWT Token for a specific user (identified by the username) that expires in one hour.
     * @param username - the unique username of the user signed in
     * @return the newly generated JWT-Token that identifies the user
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * This will check that the token matches the username inputted into the method. 
     * @param token - the JWT Token to check and compare against the username 
     * @param username - the username to vertify against the token
     * @return true if the username matches the JWTToken's stored data and the token is not expired.
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * This extracts the username from the JWT Token and returns it as a String.
     * @param token - the token to extract the username from
     * @return - the username that was stored in the token.
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
