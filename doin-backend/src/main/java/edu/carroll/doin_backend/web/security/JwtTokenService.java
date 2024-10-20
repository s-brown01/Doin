package edu.carroll.doin_backend.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * This is a Utility class to create JWT Tokens and validate them.
 *
 * <BR>
 * The creation of this class was assisted by ChatGPT
 */


@Service
public class JwtTokenService implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    /**
     * This is how long 1 hour is in milliseconds.
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour in milliseconds
    private static final long PASSWORD_RESET_EXPIRATION_TIME = 1000 * 60 * 15; // 15 minutes in milliseconds
    private static final String SECRET_KEY = "Super-Secret-Key";
    private static final String issuer = "doin";


    private final LoginRepository loginRepository;

    public JwtTokenService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    /**
     * Create a new JWT Token for a specific user (identified by the username) that expires in one hour.
     *
     * @param username - the unique username of the user signed in
     * @return the newly generated JWT-Token that identifies the user, returns null if any errors when creating
     */
    @Override
    public String generateToken(String username) {
        // try to create a JWT token, catch errors
        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("username", username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .withIssuer(issuer)
                    .sign(Algorithm.HMAC256(SECRET_KEY));
        } catch (JWTCreationException e) {
            log.error("TokenServiceImpl: generating a token resulted in a JWTCreationException: {}", e.toString());
            return null;
        } catch (IllegalArgumentException e) {
            log.error("TokenServiceImpl: generating a token resulted in an IllegalArgumentException: {}", e.toString());
            return null;
        } catch (Exception e) {
            log.error("TokenServiceImpl: generating a token resulted in UNKNOWN error: {}", e.toString());
            return null;
        }
    }

    /**
     * Validates a specific token, checking the token and the username
     *
     * @param token    - the JWT Token to check and compare against the username
     * @return true if the username matches the JWTToken's stored data and the token is not expired.
     */
    @Override
    public boolean validateToken(String token) {
        log.trace("JwtTokenServiceImpl - validating token for username: {}", getUsername(token));
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .withIssuer(issuer)
                    .build();
            // using the JWTVerifier's built-in verification method
            DecodedJWT jwt = verifier.verify(token);

            String tokenUsername = jwt.getClaim("username").asString();
            if (tokenUsername == null) {
                log.warn("TokenServiceImpl: validate token - null username");
                return false;
            }

            List<User> users = loginRepository.findByUsernameIgnoreCase(tokenUsername);
            if (users.isEmpty()) {
                log.warn("TokenServiceImpl: validate token  - no users with token's username");
                return false;
            }

            if (users.size() > 1) {
                log.warn("TokenServiceImpl: validate token - too many users found");
                return false;
            }

            return true;
        } catch (JWTVerificationException e) {
            log.warn("TokenServiceImpl: validate token - JWTVerificationException: {}", e.toString());
            // Token is invalid
            return false;
        } catch (Exception e) {
            log.warn("TokenServiceImpl: validate token - UNKNOWN error: {}", e.toString());
            return false;
        }
    }

    /**
     * Extract the username from the JWT Token.
     *
     * @param token - the token to extract the username from
     * @return - the username that was stored in the token.
     */
    @Override
    public String getUsername(String token) {
        return JWT.decode(token).getClaim("username").asString();
    }

    @Override
    public String generatePasswordResetToken(String username) {
        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("password-reset", "password-reset")
                    .withClaim("username", (Integer) null)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + PASSWORD_RESET_EXPIRATION_TIME))
                    .withIssuer(issuer)
                    .sign(Algorithm.HMAC256(SECRET_KEY));
        } catch (JWTCreationException e) {
            log.error("TokenServiceImpl: generating a passwordResertToken resulted in a JWTCreationException: {}", e.toString());
            return null;
        } catch (IllegalArgumentException e) {
            log.error("TokenServiceImpl: generating a passwordResertToken resulted in an IllegalArgumentException: {}", e.toString());
            return null;
        } catch (Exception e) {
            log.error("TokenServiceImpl: generating a passwordResertToken resulted in UNKNOWN error: {}", e.toString());
            return null;
        }
    }
}
