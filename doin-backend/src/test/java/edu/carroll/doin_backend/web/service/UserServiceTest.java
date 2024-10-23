package edu.carroll.doin_backend.web.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.carroll.doin_backend.web.model.SecurityQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import edu.carroll.doin_backend.web.security.PasswordService;
import edu.carroll.doin_backend.web.model.User;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserServiceTest {
    private static final String username = "testUser";
    private static final String password = "testPassword";

    @Autowired
    private LoginRepository loginRepo;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private SecurityQuestionRepository securityQuestionRepo;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void loadTables(){
        securityQuestionRepo.deleteAll();
        loginRepo.deleteAll();
        securityQuestionRepo.save(new SecurityQuestion(1, "pet"));
    }

    @Test
    public void validateUser(){

        // Happy
        // unsuccessful login - no one in database yet
        assertFalse(userService.validateCredentials(username, password), "validateUser: No Users - verifying that no users still returns false");

        // creating a new user, assuming it works - tested in other method
        RegisterDTO newUser = new RegisterDTO(username, password, "pet", "answer");
        assertTrue(userService.createNewUser(newUser), "validateUser: creating new user");
        assertTrue(userService.validateCredentials(username, password), "validateUser: Success - should be the same credentials and be validated");

        // Crappy
        final String invalidUsername = username + "FAKE";
        final String invalidPassword = password + "FAKE";

        assertFalse(userService.validateCredentials(username, invalidPassword), "validateUser: InvalidPassword - should not be validated with incorrect password");
        assertFalse(userService.validateCredentials(invalidUsername, password), "validateUser: InvalidUsername - should not be validated with incorrect username");
        assertFalse(userService.validateCredentials(invalidUsername, invalidPassword), "validateUser: InvalidUsernameAndPassword - should not be validated with incorrect username and password");

    }


    /*
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


    @Test
    public void createNewUser() {

    }
     */
}
