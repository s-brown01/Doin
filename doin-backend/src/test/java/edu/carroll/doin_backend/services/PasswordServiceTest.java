package edu.carroll.doin_backend.services;

import edu.carroll.doin_backend.web.service.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class PasswordServiceTest {
    private static final String password = "testPassword";

    @Autowired
    private PasswordService passwordService;

    @Test
    public void hashPasswordEncoded() {
        assertFalse("hashPasswordEncoded: password should not be the same before and after hashing", passwordService.hashPassword(password).equals(password));
    }

    @Test
    public void hashPasswordCheck() {
        assertTrue("hashPasswordCheck: password should match after hashing", passwordService.validatePassword(password, passwordService.hashPassword(password)));
    }

    @Test
    public void hashPasswordWrongPassword() {
        final String wrongPassword = "wrongPassword";
        assertFalse("hashPasswordWrongPassword: two different passwords should not match", passwordService.validatePassword(password, passwordService.hashPassword(wrongPassword)));
    }
}
