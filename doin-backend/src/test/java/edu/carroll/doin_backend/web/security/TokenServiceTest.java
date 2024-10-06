package edu.carroll.doin_backend.web.security;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@Transactional
@SpringBootTest
public class TokenServiceTest {
    private static final Logger log = LoggerFactory.getLogger(TokenServiceTest.class);

    @Autowired
    private TokenService tokenService;

    @Test
    public void generateToken() {
        log.info("generateToken - tests begin");
        final String username = "test";
        String token = tokenService.generateToken(username);
        assertNotNull("generateToken: token is null", token);

        assertTrue("generateToken: username should match", username.equals(tokenService.getUsername(token)));
    }

    @Test
    public void validateToken() {
        log.info("validateToken - tests begin");
        final String username = "test";
        String token = tokenService.generateToken(username);
        String tokenUsername = tokenService.getUsername(token);
        assertTrue("validateToken: username should match", username.equals(tokenUsername));

    }

}
