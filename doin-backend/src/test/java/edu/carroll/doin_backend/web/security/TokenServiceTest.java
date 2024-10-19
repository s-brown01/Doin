package edu.carroll.doin_backend.web.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TokenServiceTest {

    /**
     * The tokenService we're testing
     */
    @Autowired
    private TokenService tokenService;

    private static final String username = "testUsername";

    /**
     * Test for generating a token and asserting that it's not null.
     */
    @Test
    public void generateToken() {
        // Generate a token for the given username
        String token = tokenService.generateToken(username);
        // Assert that the generated token is not null
        assertNotNull(token, "Token should not be null");
        // Assert that the username extracted from the token matches the expected username
        assertEquals(username, tokenService.getUsername(token), "Username should match the token");
    }

    /**
     * Test for validating a token and ensuring the username can be extracted correctly.
     */
    @Test
    public void validateToken() {
        // Generate a token for the given username
        String token = tokenService.generateToken(username);
        // Extract the username from the token and validate it
        String tokenUsername = tokenService.getUsername(token);
        // Assert that the extracted username is not null
        assertNotNull(tokenUsername, "Token Username should not be null");
        // Assert that the extracted username matches the expected username
        assertEquals(username, tokenUsername, "Username should match the token");
        // Assert that the token is valid
        assertTrue(tokenService.validateToken(token), "Token should be valid");
    }
}
