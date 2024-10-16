package edu.carroll.doin_backend.web.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordServiceTest {
    /**
     * A password to be used for all tests (used as a correct password)
     */
    private static final String password = "testPassword";
    /**
     * A different password to be used for all tests (simulates an incorrect password)
     */
    private static final String wrongPassword = "wrongPassword";

    @InjectMocks
    private PasswordBCryptService passwordService;

    @BeforeEach
    public void setup() {
        System.out.println("TESTING PASSWORD");
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Making sure that the password service hashes the password
     */
    @Test
    public void hashPasswordEncoded() {
        // hash the password
        String result = passwordService.hashPassword(password);
        // make sure it was successfully hashed
        assertNotEquals(password, result, "hashPasswordEncoded: password should not be the same before and after hashing");
    }

    /**
     * Making sure that nulls are handled appropriately
     */
    @Test
    public void hashPasswordNull() {
        // make sure that you can't hash a null
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            passwordService.hashPassword(null);
        });
        // make sure it is IllegalArgumentException
        assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    /**
     * Make sure that the right password is validated for the correct hashing
     */
    @Test
    public void validatePasswordCheck() {
        // check that when a password is hashed it can still be validated
        final String hashedPassword = passwordService.hashPassword(password);
        // make sure the right password can be validated
        assertTrue(passwordService.validatePassword(password, hashedPassword), "validatePasswordCheck: password should match after hashing");
    }

    /**
     * Makes sure that the wrong password is not validated when to a different hashing
     */
    @Test
    public void validatePasswordWrongPassword() {
        // hash the correct password
        final String hashedPassword = passwordService.hashPassword(password);
        // make that the wrong password doesn't match a different hashed password
        assertFalse(passwordService.validatePassword(wrongPassword, hashedPassword), "validatePasswordWrongPassword: two different passwords should not match");
    }
}
