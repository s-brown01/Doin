package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class UserServiceTest {
    private static final String username1 = "testUser";
    private static final String password = "testPassword";
    final String invalidUsername = username1 + "FAKE";
    final String invalidPassword = password + "FAKE";
    final String validSQValue = "pet";
    final String invalidSQValue = validSQValue + "FAKE";
    RegisterDTO user1Data = new RegisterDTO(username1, password, validSQValue, "answer");

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityQuestionService sqService;

    @BeforeEach
    public void setUp() {
        sqService.addSecurityQuestion(validSQValue);
    }

    @Test
    public void createNewUser_Success() {
        assertTrue(userService.createNewUser(user1Data), "User1 should be created successfully");
        assertFalse(userService.createNewUser(user1Data), "User1 should not be created twice");
        assertTrue(userService.validateCredentials(username1, password), "User1 should be validated");
    }

    @Test
    public void createNewUser_Invalid() {
        final String shortPassword = "pass";
        final RegisterDTO badPassData = new RegisterDTO(username1, shortPassword, validSQValue, "answer");
        final RegisterDTO invalidSQ = new RegisterDTO(username1, password, invalidSQValue, "answer");
        final RegisterDTO takenUsername = new RegisterDTO(username1, password, validSQValue, "answer");

        assertTrue(userService.createNewUser(user1Data), "User1 should be created successfully");
        assertFalse(userService.createNewUser(takenUsername), "Duplicate username should not be created");
        assertFalse(userService.createNewUser(badPassData), "User with short password should not be created");
        assertFalse(userService.createNewUser(invalidSQ), "User should not be created successfully with invalid SQ");
    }

    @Test
    public void createNewUser_Null() {
        final RegisterDTO emptySQ = new RegisterDTO(username1, password, "", "answer");
        final RegisterDTO nullSQ = new RegisterDTO(username1, password, null, "answer");
        final RegisterDTO emptyUsername = new RegisterDTO("", password, validSQValue, "answer");
        final RegisterDTO nullUsername = new RegisterDTO(null, password, validSQValue, "answer");
        final RegisterDTO emptyPassword = new RegisterDTO(username1, "", validSQValue, "answer");
        final RegisterDTO nullPassword = new RegisterDTO(username1, null, validSQValue, "answer");
        final RegisterDTO nullData = new RegisterDTO(null, null, null, null);


        assertFalse(userService.createNewUser(emptySQ), "User should not be created successfully with empty SQ");
        assertFalse(userService.createNewUser(nullSQ), "User should not be created successfully with null SQ");
        assertFalse(userService.createNewUser(emptyUsername), "User should not be created successfully with empty username");
        assertFalse(userService.createNewUser(nullUsername), "User should not be created successfully with null username");
        assertFalse(userService.createNewUser(emptyPassword), "User should not be created successfully with empty password");
        assertFalse(userService.createNewUser(nullPassword), "User should not be created successfully with null password");
        assertFalse(userService.createNewUser(nullData), "User should not be created successfully with null data");
    }

    @Test
    public void validateCredentials_Success() {
        userService.createNewUser(user1Data);
        assertTrue(userService.validateCredentials(username1, password), "Correct username and password should return true");
    }

    @Test
    public void validateCredentials_Invalid() {
        assertFalse(userService.validateCredentials(username1, password), "No users should return false");
        userService.createNewUser(user1Data);
        final String upperCasePassword = password.toUpperCase();
        final String lowerCasePassword = password.toLowerCase();

        assertFalse(userService.validateCredentials(username1, invalidPassword), "Incorrect password should return false");
        assertFalse(userService.validateCredentials(invalidUsername, password), "Incorrect username should return false");
        assertFalse(userService.validateCredentials(invalidUsername, invalidPassword), "Incorrect credentials should return false");
        assertFalse(userService.validateCredentials(username1, upperCasePassword), "Correct upper-case password should return false");
        assertFalse(userService.validateCredentials(username1, lowerCasePassword), "Correct lower-case password should return false");
    }

    @Test
    public void validateCredentials_Null() {
        userService.createNewUser(user1Data);
        assertFalse(userService.validateCredentials(null, null), "'null' credentials should return false");
        assertFalse(userService.validateCredentials(null, password), "'null' username should return false");
        assertFalse(userService.validateCredentials(username1, null), "'null' password should return false");
    }

}
