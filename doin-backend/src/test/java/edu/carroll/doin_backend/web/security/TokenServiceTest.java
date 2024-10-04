package edu.carroll.doin_backend.web.security;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class TokenServiceTest {
    private static final Logger log = LoggerFactory.getLogger(TokenServiceTest.class);

    @Autowired
    private TokenService tokenService;

    @Test
    public void generateToken() {
        log.info("generateToken - tests begin");
        final String username = "admin";
        String token = tokenService.generateToken(username);
        assertNotNull("generateToken: token is null", token);

        assertTrue("generateToken: username should match", username.equals(tokenService.getUsername(token)));
    }

}
