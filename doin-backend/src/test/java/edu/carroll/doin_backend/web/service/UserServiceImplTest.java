package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.util.AssertionErrors.*;

@Transactional
@SpringBootTest
public class UserServiceImplTest {
    private static final String username = "testUser";
    private static final String password = "testPassword";

    @Autowired
    private UserService userService;

    @Test
    public void validateUser(){
        // Happy
        // unsuccessful login - no one in database yet
        assertFalse("validateUser: No Users - verifying that no users still returns false", userService.validateCredentials(username, password));

        // creating a new user, assuming it works - tested in other method
        RegisterDTO newUser = new RegisterDTO(username, password, "pet", "answer");
        assertTrue("validateUser: creating new user", userService.createNewUser(newUser));
        assertTrue("validateUser: Success - should be the same credentials and be validated", userService.validateCredentials(username, password));
        
        // Crappy
        final String invalidUsername = username + "FAKE";
        final String invalidPassword = password + "FAKE";

        assertFalse("validateUser: InvalidPassword - should not be validated with incorrect password", userService.validateCredentials(username, invalidPassword));
        assertFalse("validateUser: InvalidUsername - should not be validated with incorrect username", userService.validateCredentials(invalidUsername, password));
        assertFalse("validateUser: InvalidUsernameAndPassword - should not be validated with incorrect username and password", userService.validateCredentials(invalidUsername, invalidPassword));

    }

    @Test
    public void createNewUser() {

    }
}
