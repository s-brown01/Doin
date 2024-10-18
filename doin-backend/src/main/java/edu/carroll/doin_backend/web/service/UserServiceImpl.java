package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.*;
import edu.carroll.doin_backend.web.model.SecurityQuestion;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import edu.carroll.doin_backend.web.repository.UserRepository;
import edu.carroll.doin_backend.web.security.PasswordService;
import edu.carroll.doin_backend.web.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    /**
     * A Logger to just for this class
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * a LoginRepository to get the findByUsername method
     */
    private final LoginRepository loginRepo;

    /**
     * a password service to verify users
     */
    private final PasswordService passwordService;

    /**
     * a TokenService to validate tokens
     */
    private final TokenService tokenService;

    private final SecurityQuestionRepository securityQuestionRepo;
    private final UserRepository userRepository;

    /**
     * The constructor of a LoginServiceImpl. It needs a LoginRepository and a PasswordService in order. The parameters in constructor allow Springboot to automatically inject dependencies.
     *
     * @param loginRepo       - the LoginRepository which holds all registered Users
     * @param passwordService - the PasswordService to verify user's password
     */
    public UserServiceImpl(LoginRepository loginRepo,
                           PasswordService passwordService,
                           SecurityQuestionRepository securityQuestionRepo,
                           TokenService tokenService,
                           UserRepository userRepository) {
        this.loginRepo = loginRepo;
        this.passwordService = passwordService;
        this.securityQuestionRepo = securityQuestionRepo;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    /**
     * This method creates a new User in the LoginRepository based on the RegisterDTO
     * placed into the method. It will verify the username (no special characters and is
     * unique), hash the password, and hash the security question.
     *
     * @param registerDTO - the DTO that contains the necessary data to create a new user
     * @return true is a new user was created, false if it wasn't for any reason
     */
    @Override
    public boolean createNewUser(RegisterDTO registerDTO) {
        log.info("createNewUser: Attempting to create new user {}", registerDTO.getUsername());
        if (!isValidNewUsername(registerDTO.getUsername())) {
            log.info("createNewUser: Invalid username {}", registerDTO.getUsername());
            return false;
        }

        if (!isValidPassword(registerDTO.getPassword())) {
            log.info("createNewUser: Invalid password with username {}", registerDTO.getUsername());
            return false;
        }

        final Integer securityQID = securityQuestionRepo.findIdByQuestion(registerDTO.getSecurityQuestionString());
        // make sure the Security Question exists in the database
        if (securityQID == null) {
            log.warn("createNewUser: Invalid security question ID {}, new Register {}", registerDTO.getSecurityQuestionString(), registerDTO.getUsername());
            return false;
        }
        // set SecurityQuestionID
        registerDTO.setSecurityQuestionId(securityQID);
        SecurityQuestion securityQuestion = securityQuestionRepo.getReferenceById(securityQID);
        // create hashed password
        String hashedPassword = passwordService.hashPassword(registerDTO.getPassword());

        // store the hashed security answer
        registerDTO.setSecurityAnswer(passwordService.hashPassword(registerDTO.getSecurityAnswer()));

        try {
            log.info("createNewUser: validated and saving new User {}", registerDTO.getUsername());
            User newUser = new User(registerDTO, hashedPassword, securityQuestion);
            loginRepo.save(newUser);
        } catch (Exception e) {
            // make sure no error when saving/creating the user
            log.error("createNewUser: adding new User {} failed\n{}", registerDTO.getUsername(), e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * This function makes sure that a new User's username is valid: not empty/null, and not taken by another user.
     *
     * @param username - the new username to be checked
     * @return true if the username is valid, false if not valid (empty, null, non-unique).
     */
    private boolean isValidNewUsername(String username) {
        // make sure not null or empty
        if (username == null || username.isEmpty() || username.trim().isEmpty()) {
            log.warn("createNewUser: isValidNewUsername - null or empty username");
            return false;
        }

        // make sure there are no other users with this username
        if (!loginRepo.findByUsernameIgnoreCase(username).isEmpty()) {
            log.warn("createNewUser: isValidNewUsername - non-unique username");
            return false;
        }

        // making sure there's no weird characters in the name
        String[] invalidChars = new String[]{"`", "'", "/", ";", ":", "*", "{", "}", "[", "]", "|"};
        for (String s : invalidChars) {
            // indexOf(c) will return -1 if char doesn't exist
            if (username.contains(s)) {
                log.warn("createNewUser: isValidNewUsername - invalid username");
                return false;
            }
        }

        return true;
    }

    /**
     * This function makes sure that a new User's password is valid: not empty/null
     *
     * @param password - the raw password to be checked
     * @return true if the username is valid, false if not valid (null, empty).
     */
    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty() || password.trim().isEmpty()) {
            log.warn("createNewUser: isValidPassword - null or empty password");
            return false;
        }

        return true;
    }

    /**
     * This method will ensure that the username inputted is in the database and
     * that the password matches the User with that username.
     *
     * @param username - the username the person logging in is using
     * @param password - the password the person logging in is using
     * @return true if the username and password are both valid; false if 1 or both
     * are invalid.
     */
    @Override
    public boolean validateCredentials(String username, String password) {
        log.debug("validateCredentials: user '{}' attempting login", username);
        List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(username);

        // we expect 1 user found per username.
        // if we find less than 1 user...
        if (foundUsers.isEmpty()) {
            log.info("validateCredentials: user {} cannot be found in database", username);
            // checking if other similar users are in there
            List<User> extraUsers = loginRepo.findByUsernameContainingIgnoreCase(username);
            if (extraUsers.isEmpty()) {
                // no similar usernames, so just return false and no extra logs
                return false;
            }
            // if there are similar usernames, give an extra log message
            log.info("validateCredentials: similar usernames to {} found in database", username);
            return false;
        }

        // if we find more than 1 user...
        if (foundUsers.size() > 1) {
            log.warn("validateCredentials: found more than 1 user ({})", foundUsers.size());
            return false;
        }

        // made sure only 1 user, now grab it
        User user = foundUsers.get(0);

        // now validate the password using the Service's built in validator
        if (!passwordService.validatePassword(password, user.getPassword())) {
            log.debug("validateCredentials: given password did not match with user's, {}, previously stored password", username);
            return false;
        }

        // if passed all checks, then finally return true
        log.info("validateCredentials: User {} successfully validated", username);
        return true;
    }

    /**
     * Validates the provided token by checking if it corresponds to a valid user.
     * <p>
     * This method first retrieves the username associated with the given token. It then checks if there is
     * exactly one user associated with that username in the repository. If there are no users or multiple users,
     * the token is considered invalid. Finally, if a valid user is found, it checks the validity of the token
     * using the {@link TokenService}.
     * </p>
     *
     * @param tokenDTO the {@link TokenDTO} object containing the token to validate.
     * @return {@code true} if the token is valid and corresponds to exactly one user; {@code false} otherwise.
     */
    @Override
    public boolean validateToken(TokenDTO tokenDTO) {
        final String username = tokenService.getUsername(tokenDTO.getToken());
        log.info("validateToken: for user {}", username);

        final List<User> users = loginRepo.findByUsernameIgnoreCase(username);

        if (users.isEmpty()) {
            // Finding no users is expected, not horrid
            log.debug("validateToken: found no users with username {}", username);
            return false;
        }

        if (users.size() > 1) {
            // Finding more than 1 user is a bigger issue than 0 users
            log.warn("validateToken: JPA Repository found more than 1 users with username {}", username);
            return false;
        }

        // If username is validated, see if the token is valid with tokenService
        return tokenService.validateToken(tokenDTO.getToken());
    }

    @Override
    public UserDTO findUser(Integer id, String username) {
        if (id == null && username == null) {
            return null;
        }

        Optional<User> user;
        if (id != null) {
            user = userRepository.findById(id);
        } else {
            user = Optional.ofNullable(userRepository.findByUsername(username));
        }

        return user.map(UserDTO::new).orElse(null);
    }

    /**
     * Validates the security question and answer for a user during the password reset process.
     *<p>
     * This method checks if the provided username exists, verifies that there is only one user
     * associated with that username, ensures that the security question is valid, and confirms
     * that the provided security answer matches the stored answer.
     * </p>
     * <p>This javadoc was generated with help from ChatGPT</p>
     *
     * @param forgotPasswordDTO the data transfer object containing the user's username,
     *                          security question, and security answer
     * @return a {@link ValidateResult} object containing the validation outcome.
     *         If the validation fails, the result will contain a message indicating the reason for the failure.
     *         If the validation succeeds, the result will indicate success with a corresponding message.
     */
    @Override
    public ValidateResult validateSecurityQuestion(ForgotPasswordDTO forgotPasswordDTO) {
        log.info("UserServiceImpl: validateSecurityQuestion - validating for user {}", forgotPasswordDTO.getUsername());

        List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(forgotPasswordDTO.getUsername());

        // make sure the username exists in the repository
        if (foundUsers.isEmpty()) {
            log.info("validateSecurityQuestion: user {} does not exist", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Username");
        }

        // make sure there isn't more than 1 user
        if (foundUsers.size() > 1) {
            log.warn("validateSecurityQuestion: found more than 1 user with username {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Internal Error");
        }
        
        // get the security question id from the html string
        final Integer securityQID = securityQuestionRepo.findIdByQuestion(forgotPasswordDTO.getSecurityQuestionValue());
        // make sure the Security Question exists in the database
        if (securityQID == null) {
            log.warn("validateSecurityQuestion: Invalid security question ID {}, username {}", forgotPasswordDTO.getSecurityQuestionValue(), forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Internal Error");
        }

        // the user who wants to validate their password
        User resetUser = foundUsers.get(0);

        if (!resetUser.getSecurityQuestion().getId().equals(securityQID)) {
            log.warn("validateSecurityQuestion: Given security question ID did not match stored ID for user {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Security Question");
        }

        if (!passwordService.validatePassword(forgotPasswordDTO.getSecurityQuestionAnswer(), resetUser.getSecurityQuestionAnswer())) {
            log.warn("validateSecurityQuestion: the security question given did not match with the hashed answer stored for username {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Security Question");
        }

        return new ValidateResult(true, "Successfully validated");
    }
}
