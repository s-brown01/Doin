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
 * This service provides utility methods for creating and validating JWT (JSON Web Tokens) for user authentication.
 * <p>
 * The service uses HMAC256 algorithm with a secret key to generate and validate tokens. It also manages token expiration,
 * which is set to one hour. The tokens include the username as a claim and are issued by the "doin" issuer.
 * </p>
 * <p>
 * The service is used to generate a token when a user logs in, and to validate the token when making authenticated requests.
 * </p>
 */
@Service
public class JwtTokenService implements TokenService {
    /**
     * A {@link Logger} to just for this class
     */
    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);
    /**
     * This is how long 1 hour is in milliseconds.
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour in milliseconds
    /**
     * A super secret security key to encode the JWT-Tokens with
     */
    private static final String SECRET_KEY = "Super-Secret-Key";
    /**
     * The issuer of the tokens (in this case the issuer is doin)
     */
    private static final String issuer = "doin";

    /**
     * a {@link LoginRepository} that interacts with {@link User} entities
     */
    private final LoginRepository loginRepository;

    /**
     * A constructor to make a JwtTokenService
     * @param loginRepository the repository containing all Users for the site
     */
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
    public String generateToken(String username, Integer id) {
        // try to create a JWT token, catch errors
        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("username", username)
                    .withClaim("id", id)
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
    public Integer getUserId(String token) {
        return JWT.decode(token.substring(7)).getClaim("id").asInt();
    }
}
