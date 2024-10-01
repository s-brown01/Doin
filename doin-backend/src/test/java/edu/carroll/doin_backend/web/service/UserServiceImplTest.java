package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.util.AssertionErrors.*;

@Transactional
@SpringBootTest
public class UserServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImplTest.class);

    private static final String username = "testUser";
    private static final String password = "testPassword";

    @Autowired
    private UserService userService;
//
//    @Autowired
//    private PasswordService passwordService;

//    private UserDTO mockUserDTO = new UserDTO();

    @Test
    public void validateUser(){
        // Happy
        // unsuccessful login - no one in database yet
        System.out.print("\n\nTESTING\n\n");
        log.debug("validateUser: No Users - verifying that no users still returns false");
        assertFalse("validateUser: No Users - verifying that no users still returns false", userService.validateCredentials(username, password));

        // creating a new user, assuming it works - tested in other method
        RegisterDTO newUser = new RegisterDTO(username, password, "pet", "answer");
        assertTrue("validateUser: creating new user", userService.createNewUser(newUser));
        log.debug("validateUser: Success - verifying that correct credentials work");
        assertTrue("validateUser: Success - should be the same credentials and be validated", userService.validateCredentials(username, password));
        
        // Crappy
        final String invalidUsername = username + "FAKE";
        final String invalidPassword = password + "FAKE";

        log.info("validateUser: InvalidPassword - verifying that incorrect password doesn't work");
        assertFalse("validateUser: InvalidPassword - should not be validated with incorrect password", userService.validateCredentials(username, invalidPassword));

        log.info("validateUser: InvalidUsername - verifying that incorrect username doesn't work");
        assertFalse("validateUser: InvalidUsername - should not be validated with incorrect username", userService.validateCredentials(invalidUsername, password));

        log.info("validateUser: InvalidUsernameAndPassword - verifying that incorrect username and password doesn't work");
        assertFalse("validateUser: InvalidUsernameAndPassword - should not be validated with incorrect username and password", userService.validateCredentials(invalidUsername, invalidPassword));




    }

    @Test
    public void createNewUser() {
        log.info("createNewUser: Success - verifying that a new user can be created");

    }
}
