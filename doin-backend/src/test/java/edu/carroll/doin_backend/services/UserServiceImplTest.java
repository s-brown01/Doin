package edu.carroll.doin_backend.services;

import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.service.PasswordService;
import edu.carroll.doin_backend.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

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

    private UserDTO mockUserDTO = new UserDTO();

    @Test
    public void validateUserSuccess() {
        log.info("validateUserSuccess: verifying that correct credentials work");
        assertTrue("validateUserSuccess: should be the same credentials and be validated", userService.validateCredentials(username, password));
    }

    @Test
    public void validateUserFailureInvalidPassword() {
        log.info("validateUserFailureInvalidPassword: verifying that incorrect password doesn't work");
        assertFalse("validateUserFailureInvalidPassword: should not be validated with incorrect password", userService.validateCredentials(username, password + "FAKE"));
    }

    @Test
    public void validateUserFailureInvalidUsername() {
        log.info("validateUserFailureInvalidUsername: verifying that incorrect username doesn't work");
        assertFalse("validateUserFailureInvalidUsername: should not be validated with incorrect username", userService.validateCredentials(username + "FAKE", password));
    }

    @Test
    public void validateUserFailureInvalidUsernameAndPassword() {
        log.info("validateUserFailureInvalidUsernameAndPassword: verifying that incorrect username and password doesn't work");
        assertFalse("validateUserFailureInvalidUsernameAndPassword: should not be validated with incorrect username and password", userService.validateCredentials(username + "FAKE", password + "FAKE"));
    }

    @Test
    public void createNewUserSuccess() {
        log.info("createNewUserSuccess: verifying that a new user can be created");

    }
}
