package edu.carroll.doin_backend.services;

import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.service.LoginService;
import edu.carroll.doin_backend.web.service.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class LoginServiceImplTest {
    private static final String username = "testUser";
    private static final String password = "testPassword";

    @Autowired
    private LoginService loginService;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordService passwordService;

    private User mockUser;

    @BeforeEach
    public void beforeTest() {
        assertNotNull("loginRespository must be injected", loginRepository);
        assertNotNull("loginService must be injected", loginService);
        assertNotNull("passwordService must be injected", passwordService);

        mockUser = new User(username, passwordService.hashPassword(password));

        final List<User> users = loginRepository.findByUsernameIgnoreCase(username);
        if (users.isEmpty()) {
            loginRepository.save(mockUser);
        }
    }

    @Test
    public void validateUserSuccess() {
        assertTrue("validateUserSuccess: should be the same credentials and be validated", loginService.validateUser(username, password));
    }

    @Test
    public void validateUserFailureInvalidPassword() {
        assertFalse("validateUserFailureInvalidPassword: should not be validated with incorrect password", loginService.validateUser(username, password + "FAKE"));
    }

    @Test
    public void validateUserFailureInvalidUsername() {
        assertFalse("validateUserFailureInvalidUsername: should not be validated with incorrect username", loginService.validateUser(username + "FAKE", password));
    }

    @Test
    public void validateUserFailureInvalidUsernameAndPassword() {
        assertFalse("validateUserFailureInvalidUsernameAndPassword: should not be validated with incorrect username and password", loginService.validateUser(username + "FAKE", password + "FAKE"));
    }
}
