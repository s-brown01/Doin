package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.ForgotPasswordDTO;
import edu.carroll.doin_backend.web.dto.LoginDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;

import edu.carroll.doin_backend.web.dto.TokenDTO;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.SecurityQuestionService;
import edu.carroll.doin_backend.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LoginControllerTest {
    private final String username1 = "username_1";
    private final String password1 = "password_1";
    private final String username2 = "username_2";
    private final String password2 = "password_2";
    private final String invalidUsername = "invalid username";
    private final String invalidPassword = "invalid password";
    private final String invalidSQ = "invalid SQ";
    private final String unusedUsername = "No_user_with_this_username";
    private final String unusedPassword = "No_user_with_this_password";
    private final String sqQuestion = "security_question";

    @Autowired
    private LoginController loginController;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityQuestionService sqService;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        sqService.addSecurityQuestion(sqQuestion);
        createNewUser(username1, password1);
    }

    private void createNewUser(String username, String password) {
        RegisterDTO data = new RegisterDTO(username, password, sqQuestion, "answer");
        userService.createNewUser(data);
    }

    @Test
    public void loginAttempt_Success() {
        LoginDTO user1Login = new LoginDTO(username1, password1);
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(user1Login);
        assertEquals(HttpStatus.OK, loginAttempt.getStatusCode());
        assertNotNull(loginAttempt.getBody());
        final String token = loginAttempt.getBody().getToken();
        assertTrue(tokenService.validateToken(token));
        assertEquals(username1, tokenService.getUsername(token));
    }

    @Test
    public void loginAttempt_UnusedUsername() {
        LoginDTO unusedUsernameLogin = new LoginDTO(unusedUsername, password1);
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(unusedUsernameLogin);
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void loginAttempt_InvalidUsername() {
        LoginDTO invalidUsernameLogin = new LoginDTO(invalidUsername, password1);
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(invalidUsernameLogin);
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void loginAttempt_IncorrectPassword() {
        LoginDTO user1IncorrectPwd = new LoginDTO(username1, unusedPassword);
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(user1IncorrectPwd);
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void loginAttempt_EmptyUsername() {
        LoginDTO emptyUsernameLogin = new LoginDTO("", password1);
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(emptyUsernameLogin);
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void loginAttempt_EmptyPassword() {
        LoginDTO emptyPasswordLogin = new LoginDTO(username1, "");
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(emptyPasswordLogin);
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void loginAttempt_NullUsername() {
        LoginDTO nullUsernameLogin = new LoginDTO(null, password1);
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(nullUsernameLogin);
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void loginAttempt_NullPassword() {
        LoginDTO nullPasswordLogin = new LoginDTO(username1, null);
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(nullPasswordLogin);
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void loginAttempt_NullData() {
        LoginDTO nullData = new LoginDTO(null, null);
        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(nullData);
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_Success() {
        RegisterDTO successfulRegister = new RegisterDTO(username2, password2, sqQuestion, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(successfulRegister);
        assertEquals(HttpStatus.OK, registerAttempt.getStatusCode());
        assertNotNull(registerAttempt.getBody());
        assertEquals("{}", registerAttempt.getBody());

        ResponseEntity<TokenDTO> loginTest = loginController.loginAttempt(new LoginDTO(username2, password2));
        assertEquals(HttpStatus.OK, loginTest.getStatusCode());
        assertNotNull(loginTest.getBody());
        final String token = loginTest.getBody().getToken();
        assertTrue(tokenService.validateToken(token));
        assertEquals(username1, tokenService.getUsername(token));
    }

    @Test
    public void registerUser_InvalidUsername() {
        RegisterDTO invalidUsernameRegister = new RegisterDTO(invalidUsername, password2, sqQuestion, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(invalidUsernameRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(invalidUsername, password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_InvalidPassword() {
        RegisterDTO invalidPasswordRegister = new RegisterDTO(username2, invalidPassword, sqQuestion, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(invalidPasswordRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, invalidPassword));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_InvalidSecurityQuestion() {
        RegisterDTO invalidSQRegister = new RegisterDTO(username2, password2, invalidSQ, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(invalidSQRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_EmptyUsername() {
        RegisterDTO emptyUsernameRegister = new RegisterDTO("", password2, sqQuestion, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(emptyUsernameRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO("", password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_EmptyPassword() {
        RegisterDTO emptyPasswordRegister = new RegisterDTO(username2, "", sqQuestion, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(emptyPasswordRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, ""));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_EmptySecurityQuestion() {
        RegisterDTO emptySecurityQuestionRegister = new RegisterDTO(username2, password2, "", "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(emptySecurityQuestionRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_EmptySecurityAnswer() {
        RegisterDTO emptySecurityQuestionAnswerRegister = new RegisterDTO(username2, password2, sqQuestion, "");
        ResponseEntity<String> registerAttempt = loginController.registerUser(emptySecurityQuestionAnswerRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_NullUsername() {
        RegisterDTO nullUsernameRegister = new RegisterDTO(null, password2, sqQuestion, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(nullUsernameRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(null, password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_NullPassword() {
        RegisterDTO nullPasswordRegister = new RegisterDTO(username2, null, sqQuestion, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(nullPasswordRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, null));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_NullSecurityQuestion() {
        RegisterDTO nullSecurityQuestionRegister = new RegisterDTO(username2, password2, null, "answer");
        ResponseEntity<String> registerAttempt = loginController.registerUser(nullSecurityQuestionRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_NullSecurityAnswer() {
        RegisterDTO nullSecurityAnswerRegister = new RegisterDTO(username2, password2, sqQuestion, null);
        ResponseEntity<String> registerAttempt = loginController.registerUser(nullSecurityAnswerRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void registerUser_NullData() {
        RegisterDTO nullDataRegister = new RegisterDTO(null, null, null, null);
        ResponseEntity<String> registerAttempt = loginController.registerUser(nullDataRegister);
        assertEquals(HttpStatus.BAD_REQUEST, registerAttempt.getStatusCode());
        assertNull(registerAttempt.getBody());
        assertEquals("Invalid data", registerAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(null, null));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void forgotPassword_Success() {
        RegisterDTO successfulRegister = new RegisterDTO(username2, password1, sqQuestion, "answer");
        // creating a new user
        ResponseEntity<String> registerAttempt = loginController.registerUser(successfulRegister);
        assertEquals(HttpStatus.OK, registerAttempt.getStatusCode());
        assertNotNull(registerAttempt.getBody());
        assertEquals("{}", registerAttempt.getBody());

        ForgotPasswordDTO forgotPwdSuccess = new ForgotPasswordDTO(username2, sqQuestion, "answer", password2);
        ResponseEntity<String> resetAttempt = loginController.forgotPassword(forgotPwdSuccess);
        assertEquals(HttpStatus.OK, resetAttempt.getStatusCode());
        assertNotNull(resetAttempt.getBody());
        assertEquals("{}", resetAttempt.getBody());

        final ResponseEntity<TokenDTO> loginAttempt = loginController.loginAttempt(new LoginDTO(username2, password2));
        assertEquals(HttpStatus.UNAUTHORIZED, loginAttempt.getStatusCode());
        assertNull(loginAttempt.getBody());
    }

    @Test
    public void forgotPassword_InvalidUsername() {}

    @Test
    public void forgotPassword_EmptyUsername() {}

    @Test
    public void forgotPassword_EmptyPassword() {}

    @Test
    public void forgotPassword_InvalidSecurityQuestion() {}

    @Test
    public void forgotPassword_EmptySecurityQuestion() {}

    @Test
    public void forgotPassword_EmptySecurityAnswer() {}

    @Test
    public void forgotPassword_NullUsername() {}

    @Test
    public void forgotPassword_NullPassword() {}

    @Test
    public void forgotPassword_NullSecurityQuestion() {}

    @Test
    public void forgotPassword_NullSecurityAnswer() {}

    @Test
    public void forgotPassword_NullData() {}


}
