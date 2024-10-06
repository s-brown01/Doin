package edu.carroll.doin_backend.web.security;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@Transactional
@SpringBootTest
public class PasswordServiceTest {
    private static final Logger log = LoggerFactory.getLogger(PasswordServiceTest.class);
    private static final String password = "testPassword";

    @Autowired
    private PasswordService passwordService;

    @Test
    public void hashPasswordEncoded() {
        log.info("hashPasswordEncoded: testing that hashing works");
        assertFalse("hashPasswordEncoded: password should not be the same before and after hashing", passwordService.hashPassword(password).equals(password));
    }

    @Test
    public void hashPasswordNull() {
        log.info("hashPasswordNull: testing that hashing does not return null");
        assertNotNull("hashPasswordNull: password should not be null", passwordService.hashPassword(password));
    }


    @Test
    public void validatePasswordCheck() {
        log.info("validatePasswordCheck: testing that validatePassword works with correct password");
        assertTrue("validatePasswordCheck: password should match after hashing", passwordService.validatePassword(password, passwordService.hashPassword(password)));
    }

    @Test
    public void validatePasswordWrongPassword() {
        log.info("validatePasswordWrongPassword: testing that incorrect credentials are not validated");
        final String wrongPassword = "wrongPassword";
        assertFalse("validatePasswordWrongPassword: two different passwords should not match", passwordService.validatePassword(password, passwordService.hashPassword(wrongPassword)));
    }
}
