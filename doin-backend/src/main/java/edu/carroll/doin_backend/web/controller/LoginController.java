package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.*;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class LoginController {
    /**
     * A {@link Logger} for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    /**
     * A {@link TokenService} for functions relating to JWT-Tokens
     */
    private final TokenService tokenService;
    /**
     * A {@link UserService} for functions relating to the User repository
     */
    private final UserService userService;

    /**
     * Constructor for a new LoginController
     *
     * @param tokenService the service responsible for token validations.
     * @param userService the service responsible for user and login operations
     */
    public LoginController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    /**
     * Handles login requests by validating user credentials and generating a JWT-Token
     *      upon successful authentication. Logs the process at various steps, including
     *      attempts, successes, and errors.
     *
     * @param login A {@link LoginDTO} object containing the user's login information
     *                  (username and password).
     * @return A {@link ResponseEntity} containing a {@link TokenDTO} with the generated
     *              JWT token if login is successful, or a 401 Unauthorized status if
     *              login fails or an error occurs.
     */
  @PostMapping("/login")
  public ResponseEntity<TokenDTO> loginAttempt(@RequestBody LoginDTO login) {
    log.info("LoginController: user {} attempting login", login.getUsername());
    boolean isValidUser = false;
    try {
        isValidUser = userService.validateCredentials(login.getUsername(), login.getPassword());
    } catch (Exception e) {
        log.error("LoginController: user {} login errored {}", login.getUsername(), e.getStackTrace());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    // if the user is validated by our loginService...
    if (isValidUser) {
        log.info("LoginController: user {} successfully logged in, generating JWT-Tokens", login.getUsername());
        // generate new token and store it in DTO
        UserDTO user = userService.findUser( null, login.getUsername());
        final String token = tokenService.generateToken(user.getUsername(), user.getId());
        final TokenDTO tokenDTO = new TokenDTO(token);
        // Return the DTO
        return ResponseEntity.ok(tokenDTO);
    // if NOT valid...
    } else {
        log.info("LoginController: user {} failed to log in", login.getUsername());
        // return that the username or password is invalid, no more specific than that to not reveal info
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
  }

    /**
     * Handles user registration requests by creating a new user based on the provided registration details.
     * Logs the registration process, including both successful and failed attempts.
     *
     * @param register A {@link RegisterDTO} object containing the new user's registration information
     *                 (e.g., username, password, email).
     * @return A {@link ResponseEntity} containing a success message if registration is successful, or a
     *         406 Not Acceptable status with an error message if registration fails (e.g., invalid username or password).
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO register) {
        log.info("LoginController: new user {} registering", register.getUsername());
        boolean registered = userService.createNewUser(register);
        // if the user is validated by our loginService...
        if (registered) {
            log.info("LoginController: user {} successfully registered", register.getUsername());
            return ResponseEntity.ok("{}");
        } else {
            log.warn("LoginController: user {} failed to register", register.getUsername());
            // return that the username or password is invalid, no more specific than that to not reveal info
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data");
        }
    }

    /**
     * Handles forgot password requests by validating the user's security questions and, if valid,
     * proceeding with a password reset. Logs each step of the process, including successful
     * validations and failures.
     *
     * @param forgotPasswordDTO A {@link ForgotPasswordDTO} object containing the user's username
     *                          and answers to security questions.
     * @return A {@link ResponseEntity} containing a success message if password reset is successful,
     *         a 406 Not Acceptable status if security question validation fails, or a 401 Unauthorized
     *         status if password reset fails after successful security question validation.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        log.info("LoginController: validating security questions for username {}", forgotPasswordDTO.getUsername());
        // make sure the security questions are completely valid
        ValidateResult securityQuestionResult = userService.validateSecurityQuestion(forgotPasswordDTO);
        if (!securityQuestionResult.isValid()) {
            log.warn("LoginController: forgotPassword - username {} failed their security question", forgotPasswordDTO.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid data");
        }
        // ONLY AFTER SQ was validated
        ValidateResult passwordResetResult = userService.resetPassword(forgotPasswordDTO);
        if (passwordResetResult.isValid()) {
            log.info("LoginController: forgotPassword - username {} reset their password", forgotPasswordDTO.getUsername());
            return ResponseEntity.ok("{}");
        } else {
            log.warn("LoginController: forgotPassword - username {} failed resetting their password", forgotPasswordDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid data");
        }
    }
}
