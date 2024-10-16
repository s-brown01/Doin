package edu.carroll.doin_backend.web.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import edu.carroll.doin_backend.web.security.PasswordService;
import edu.carroll.doin_backend.web.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class UserServiceImplTest {
    private static final String username = "testUser";
    private static final String password = "testPassword";

    @Mock
    private LoginRepository loginRepo;

    @Mock
    private PasswordService passwordService;

    @Mock
    private SecurityQuestionRepository securityQuestionRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        System.out.println("TESTING USER SERVICE");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void validateUser_Happy() {
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPasswordHash(passwordService.hashPassword(password));

        when(loginRepo.findByUsernameIgnoreCase(username)).thenReturn(List.of(mockUser));
        when(passwordService.validatePassword(password, mockUser.getPassword())).thenReturn(true);

        boolean result = userService.validateCredentials(username, password);
        assertTrue(result, "UserServiceImpl: validateUser_Happy - User should be validated successfully in happy path");
    }

    @Test
    public void validateUser_InvalidPassword() {
        final String invalidPassword = "invalidPassword";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPasswordHash(passwordService.hashPassword(password));

        when(loginRepo.findByUsernameIgnoreCase(username)).thenReturn(List.of(mockUser));
        when(passwordService.validatePassword(invalidPassword, mockUser.getPassword())).thenReturn(false);

        boolean result = userService.validateCredentials(username, invalidPassword);
        assertFalse(result, "UserServiceImpl: validateUser_InvalidPassword - User should not be validated with an incorrect password");

    }

    @Test
    public void validateCredentials_InvalidUsername() {
        final String invalidUsername = "invalidUsername";
        when(loginRepo.findByUsernameIgnoreCase(invalidUsername)).thenReturn(Collections.emptyList());

        boolean result = userService.validateCredentials(invalidUsername, password);

        assertFalse(result, "User should not be validated with an incorrect username");
    }


    @Test
    public void validateCredentials_InvalidUsernameAndPassword() {
        final String invalidUsername = "invalidUsername";
        final String invalidPassword = "invalidPassword";
        when(loginRepo.findByUsernameIgnoreCase(invalidUsername)).thenReturn(Collections.emptyList());

        boolean result = userService.validateCredentials(invalidUsername, invalidPassword);

        assertFalse(result, "User should not be validated with incorrect username and password");
    }

    @Test
    public void validateCredentials_NullPassword() {
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPasswordHash(null);

        when(loginRepo.findByUsernameIgnoreCase(username)).thenReturn(List.of(mockUser));

        boolean result = userService.validateCredentials(username, null);

        assertFalse(result, "User validation should fail if the password is null");
    }

    @Test
    public void validateCredentials_NullUsername() {
        User mockUser = new User();
        mockUser.setUsername(null);
        mockUser.setPasswordHash(passwordService.hashPassword(password));

        when(loginRepo.findByUsernameIgnoreCase(username)).thenReturn(Collections.emptyList());
        boolean result = userService.validateCredentials(null, password);
        assertFalse(result, "User validation should fail if the username is null");
    }

    @Test
    public void validateCredentials_NullPasswordAndUsername() {
        User mockUser = new User();
        mockUser.setUsername(null);
        mockUser.setPasswordHash(null);

        when(loginRepo.findByUsernameIgnoreCase(username)).thenReturn(Collections.emptyList());
        boolean result = userService.validateCredentials(username, password);
        assertFalse(result, "User validation should fail if the username and password is null");
    }

    /*
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
     */
}
