package edu.carroll.doin_backend.web.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.carroll.doin_backend.web.model.SecurityQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@SpringBootTest
@Transactional
public class UserServiceTest {
    private static final String username = "testUser";
    private static final String password = "testPassword";

    @Autowired
    private UserService userService;

    @Test
    public void validateCredentials(){
        // Happy
        // unsuccessful login - no one in database yet
        assertFalse(userService.validateCredentials(username, password), "validateCredentials: No Users - verifying that no users still returns false");

        // creating a new user, assuming it works - tested in other method
        RegisterDTO newUser = new RegisterDTO(username, password, "pet", "answer");
        assertTrue(userService.createNewUser(newUser), "validateCredentials: creating new user");
        assertTrue(userService.validateCredentials(username, password), "validateCredentials: Success - should be the same credentials and be validated");

        // Crappy
        final String invalidUsername = username + "FAKE";
        final String invalidPassword = password + "FAKE";
        final String upperCasePassword = password.toUpperCase();
        final String lowerCasePassword = password.toLowerCase();

        assertFalse(userService.validateCredentials(invalidUsername, password), "validateCredentials: InvalidUsername - should not be validated with incorrect username");
        assertFalse(userService.validateCredentials(invalidUsername, invalidPassword), "validateCredentials: InvalidUsernameAndPassword - should not be validated with incorrect username and password");
        assertFalse(userService.validateCredentials(username, invalidPassword), "validateCredentials: InvalidPassword - should not be validated with incorrect password");
        assertFalse(userService.validateCredentials(username, upperCasePassword), "validateCredentials: InvalidPassword - should not be validated with upper case password");
        assertFalse(userService.validateCredentials(username, lowerCasePassword), "validateCredentials: InvalidPassword - should not be validated with lower case password");

        // Crazy

        assertFalse(userService.validateCredentials(null, null), "validateCredentials: NullCredentials - should not validate null credentials");
        assertFalse(userService.validateCredentials(null, password), "validateCredentials: NullPassword - should not validate null username");
        assertFalse(userService.validateCredentials(username, null), "validateCredentials: NullUsername - should not validate null username");
    }

    @Test
    public void createNewUser(){
        // Happy
        final String validQuestion = "pet";
        final String validAnswer = "answer";
        // creating a new user with a valid questions and answers
        final RegisterDTO newUser = new RegisterDTO(username, password, validQuestion, validAnswer);
        assertTrue(userService.createNewUser(newUser), "createNewUser: creating new user");
        // trying to create the same user twice
        assertFalse(userService.createNewUser(newUser), "createNewUser: should not create newUser twice");

        // Crappy
        // a username for the new user, a security question not in the entity, and an empty string for the answer
        final String newUsername = "new" + username;
        final String badQuestion = "invalid security question";
        final String emptyAnswer = "";
        // creating a new user with a null security question
        final RegisterDTO newBadUser = new RegisterDTO(newUsername, password, badQuestion, emptyAnswer);
        assertFalse(userService.createNewUser(newBadUser), "createNewUser: should not create a user with invalid SecurityQuestion");

        // Crazy
        // try to create a user with null data
        final RegisterDTO newNullUser = new RegisterDTO(null, null, null, null);
        assertFalse(userService.createNewUser(newNullUser), "createNewUser: should not create a user with null values");
    }

}
