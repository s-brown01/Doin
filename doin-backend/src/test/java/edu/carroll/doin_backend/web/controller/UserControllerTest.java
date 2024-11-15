package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.security.JwtTokenService;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.SecurityQuestionService;
import edu.carroll.doin_backend.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserControllerTest {
    private final String username = "test_user";
    private String userHeader;
    private String invalidAuthHeader;
    private UserDTO testUser;

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityQuestionService sqService;


    @BeforeEach
    public void setUp() {
        // Create test user
        // Add security question
        sqService.addSecurityQuestion("test question");

        // Create test users
        createNewUser(username);

        // Generate auth tokens
        final String user1Token = tokenService.generateToken(username, userService.findUser(null, username).getId());

        final String invalidToken = user1Token + "INVALID";
        userHeader = "Bearer " + user1Token;

        invalidAuthHeader = "Bearer " + invalidToken;

        // Set up user1 data
        testUser = new UserDTO();
        testUser.setId(userService.findUser(null, username).getId());
        testUser.setUsername(username);

    }

    private void createNewUser(String username) {
        RegisterDTO data = new RegisterDTO(username, "password1", "test question", "answer");
        userService.createNewUser(data);
    }

    // Test for `getUser`
    @Test
    public void getUser_ById_Success() {
        Integer userId = testUser.getId();

        // Retrieve user by ID
        ResponseEntity<UserDTO> response = userController.getUser(userId, null);

        // Verify success response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return OK status");
        assertNotNull(response.getBody(), "Retrieved user should not be null");
        assertEquals(userId, response.getBody().getId(), "User ID should match");
        assertEquals(username, response.getBody().getUsername(), "Username should match");
    }

    @Test
    public void getUser_ByUsername_Success() {
        // Retrieve user by username
        ResponseEntity<UserDTO> response = userController.getUser(null, username);

        // Verify success response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return OK status");
        assertNotNull(response.getBody(), "Retrieved user should not be null");
        assertEquals(testUser.getId(), response.getBody().getId(), "User ID should match");
        assertEquals(username, response.getBody().getUsername(), "Username should match");
    }

    @Test
    public void getUser_NotFound() {
        // Attempt to retrieve non-existent user
        ResponseEntity<UserDTO> response = userController.getUser(999999, "nonexistent_user");

        // Verify not found response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return NOT_FOUND status");
        assertNull(response.getBody(), "Response body should be null for non-existent user");
    }

    // Test for `updateProfileImage`
    @Test
    public void updateProfileImage_Success() {
        // Create a mock file for profile image
        MockMultipartFile file = new MockMultipartFile("file", "profile.png", "image/png", "image data".getBytes());

        // Update profile image
        boolean result = userController.updateProfileImage(file, userHeader);

        // Verify success response
        assertTrue(result, "Profile image should be updated successfully");
    }
}