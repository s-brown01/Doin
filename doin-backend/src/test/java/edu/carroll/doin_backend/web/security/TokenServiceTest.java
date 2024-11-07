package edu.carroll.doin_backend.web.security;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.model.SecurityQuestion;
import edu.carroll.doin_backend.web.service.SecurityQuestionService;
import edu.carroll.doin_backend.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for the {@link TokenService} to ensure token generation and validation.
 */
@SpringBootTest
@Transactional
public class TokenServiceTest {

    /**
     * The {@link TokenService} instance being tested.
     */
    @Autowired
    private TokenService tokenService;

    /**
     * The {@link UserService} instance used for user-related operations in tests.
     */
    @Autowired
    private UserService userService;

    /**
     * The {@link SecurityQuestionService} instance used for security question-related operations in tests.
     */
    @Autowired
    private SecurityQuestionService securityQuestionService;

    /**
     * A sample username used for testing purposes.
     */
    private static final String username = "testUsername";

    /**
     * A sample security question value used for testing purposes.
     */
    private static final String sqValue = "pet";

    /**
     * Sets up the test environment by adding a security question and creating a new user
     * before each test case.
     */
    @BeforeEach
    public void setUp() {
        securityQuestionService.addSecurityQuestion(sqValue);
        userService.createNewUser(new RegisterDTO(username, "password", sqValue, "answer"));
    }

    /**
     * Test for successfully generating a token and asserting that it's not null.
     * <p>
     * This test verifies that the method {@link TokenService#generateToken(String, Integer)}
     * successfully creates a token for a given username. It checks that the generated
     * token is not null and that the username extracted from the token matches the
     * expected username.
     * </p>
     */
    @Test
    public void generateToken_Successful() {
        // Generate a token for the given username
        String token = tokenService.generateToken(username, 1);
        // Assert that the generated token is not null
        assertNotNull(token, "Token should not be null");
        // Assert that the username extracted from the token matches the expected username
        assertEquals(username, tokenService.getUsername(token), "Username should match the token");
    }

    /**
     * Test for validating a token and ensuring the username can be extracted correctly.
     * This tests {@link TokenService#generateToken(String, Integer)}
     */
    @Test
    public void validateToken_Successful() {
        // Generate a token for the given username
        String token = tokenService.generateToken(username, 1);
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
