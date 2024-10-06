package edu.carroll.doin_backend.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenService implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    public static void main(String[] args){
        JwtTokenService tokenService = new JwtTokenService();
        System.out.println(tokenService.generateToken("test"));
    }

    private final String secret = "Super-Secret-Key";

    /**
     * This is how long 1 hour is in milliseconds.
     */
    private static final long HOUR_MILLIS = 60 * 60 * 1000;

    private static final String issuer = "dointertasetsdtr asdrasr";

    @Override
    public String generateToken(String username) {
        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("username", username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + HOUR_MILLIS))
                    .withIssuer(issuer)
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException e) {
            log.error("TokenServiceImpl: generating a token resulted in a JWTCreationException: {}", e.toString());
            return null;
        } catch (IllegalArgumentException e) {
            log.error("TokenServiceImpl: generating a token resulted in an IllegalArgumentException: {}", e.toString());
            return null;
        }
    }

    @Override
    public boolean validateToken(String token, String username) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject(username)
                    .withIssuer(issuer)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            String tokenUsername = jwt.getClaim("username").asString();
            if (!tokenUsername.equals(username)) {
                log.warn("TokenServiceImpl: validate token - invalid token, wrong username");
                return false;
            }
            if (tokenUsername == null) {
                log.warn("TokenServiceImpl: validate token - null username");
                return false;
            }

            return true;
        } catch (JWTVerificationException exception) {
            // Token is invalid
            return false;
        }
    }

    @Override
    public String getUsername(String token) {
        return JWT.decode(token).getClaim("username").asString();
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("username")
                .withIssuer(issuer)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

}
