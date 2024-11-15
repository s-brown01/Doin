package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.ForgotPasswordDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.model.SecurityQuestion;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.security.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    /**
     * A {@link Logger} to just for this class
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    /**
     * a {@link LoginRepository} for interactions User entities
     */
    private final LoginRepository loginRepo;
    /**
     * a {@link PasswordService} service to verify users
     */
    private final PasswordService passwordService;
    /**
     * an {@link ImageService} to interact with the Image repository
     */
    private final ImageService imageService;
    /**
     * a SecurityQuestionSerivce to interact with the SecurityQuestion JPA-Repository
     */
    private final SecurityQuestionService sqService;

    /**
     * The constructor of a LoginServiceImpl. It needs a LoginRepository and a PasswordService in order.
     * The parameters in constructor allow Springboot to automatically inject dependencies.
     *
     * @param loginRepo       - the LoginRepository which holds all registered Users
     * @param passwordService - the PasswordService to verify user's password
     * @param sqService       - the SecurityQuestionService to handle security questions
     * @param imageService    - the ImageService to handle image uploads
     */
    public UserServiceImpl(LoginRepository loginRepo,
                           PasswordService passwordService,
                           SecurityQuestionService sqService,
                           ImageService imageService) {
        this.loginRepo = loginRepo;
        this.passwordService = passwordService;
        this.sqService = sqService;
        this.imageService = imageService;
    }

    /**
     * This method creates a new User in the LoginRepository based on the RegisterDTO
     * placed into the method. It will verify the username (no special characters and is
     * unique), hash the password, and hash the security question.
     *
     * @param registerDTO - the DTO that contains the necessary data to create a new user
     * @return true if a new user was created, false if it wasn't for any reason
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
        final SecurityQuestion userSecurityQuestion = sqService.getSecurityQuestionByValue(registerDTO.getSecurityQuestionString());
        if (userSecurityQuestion == null) {
            log.info("createNewUser: Invalid security question {}", registerDTO.getSecurityQuestionString());
            return false;
        }
        // create hashed password
        String hashedPassword = passwordService.hashPassword(registerDTO.getPassword());
        // store the hashed security answer
        registerDTO.setSecurityAnswer(passwordService.hashPassword(registerDTO.getSecurityAnswer()));
        try {
            log.info("createNewUser: validated and saving new User {}", registerDTO.getUsername());
            User newUser = new User(registerDTO, hashedPassword, userSecurityQuestion);
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
        if (password == null || password.isEmpty() || password.isBlank()) {
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
        if (username == null || password == null) {
            log.warn("validateCredentials: username or password is null");
            return false;
        }
        List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(username);
        // we expect 1 user found per username.
        // if we find less than 1 user...
        if (foundUsers.isEmpty()) {
            log.info("validateCredentials: user {} cannot be found in database", username);
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
     * Finds a user by either their ID or username.
     *
     * @param id       the unique identifier of the user (optional).
     * @param username the username of the user (optional).
     * @return a {@link UserDTO} containing the user's information, or null if no user is found.
     */
    @Override
    public UserDTO findUser(Integer id, String username) {
        if (id == null && username == null) {
            return null;
        }
        Optional<User> user;
        if (id != null) {
            user = loginRepo.findById(id);
        } else {
            user = Optional.ofNullable(loginRepo.findByUsernameIgnoreCase(username).get(0));
        }
        return user.map(UserDTO::new).orElse(null);
    }

    /**
     * Updates the user's profile picture by saving the uploaded file and associating it with the user.
     *
     * @param userId   the ID of the user whose profile picture is to be updated.
     * @param file     the profile picture file to be uploaded.
     * @return true if the profile picture was successfully updated, false otherwise.
     */
    @Override
    public boolean updateProfilePicture(Integer userId, MultipartFile file) {
        Optional<User> userOpt = loginRepo.findById(userId);
        if (userOpt.isEmpty())
            return false;
        User user = userOpt.get();
        Image img;
        try {
            img = imageService.save(file);
        } catch (Exception e) {
            return false;
        }
        user.setProfilePicture(img);

        try {
            loginRepo.save(user);
            return true;
        } catch (Exception e) {
            return false; // Handle user saving error
        }
    }

    /**
     * Validates the security question and answer for a user during the password reset process.
     * <p>
     * This method checks if the provided username exists, verifies that there is only one user
     * associated with that username, ensures that the security question is valid, and confirms
     * that the provided security answer matches the stored answer.
     * </p>
     *
     * @param forgotPasswordDTO the data transfer object containing the user's username,
     *                          security question, and security answer
     * @return a {@link ValidateResult} object containing the validation outcome.
     * If the validation fails, the result will contain a message indicating the reason for the failure.
     * If the validation succeeds, the result will indicate success with a corresponding message.
     */
    @Override
    public ValidateResult validateSecurityQuestion(ForgotPasswordDTO forgotPasswordDTO) {
        log.info("UserServiceImpl: validateSecurityQuestion - validating for user {}", forgotPasswordDTO.getUsername());
        List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(forgotPasswordDTO.getUsername());

        // make sure the username exists in the repository
        if (foundUsers.isEmpty()) {
            log.warn("validateSecurityQuestion: user {} does not exist", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Username");
        }
        // make sure there isn't more than 1 user
        if (foundUsers.size() > 1) {
            log.warn("validateSecurityQuestion: found more than 1 user with username {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Internal Error");
        }
        log.trace("validateSecurityQuestion: retrieving security question {} from service for user {}", forgotPasswordDTO.getSecurityQuestionValue(), forgotPasswordDTO.getUsername());
        final SecurityQuestion userSecurityQuestion = sqService.getSecurityQuestionByValue(forgotPasswordDTO.getSecurityQuestionValue());
        if (userSecurityQuestion == null) {
            log.warn("validateSecurityQuestion: invalid security question {} for user {}", forgotPasswordDTO.getSecurityQuestionValue(), forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Security Question");
        }
        log.trace("validateSecurityQuestion: successfully retrieved security question {} from service for user {}", userSecurityQuestion.getQuestion(), forgotPasswordDTO.getUsername());
        // the user who wants to validate their password
        final User resetUser = foundUsers.get(0);
        // validating the stored SQ-ID matches the SQ-ID selected by the user in the forgot password page
        log.trace("validateSecurityQuestion: validating the securityQuestion for user {}", resetUser.getUsername());
        if (!resetUser.getSecurityQuestion().equals(userSecurityQuestion)) {
            log.warn("validateSecurityQuestion: Given security question ID did not match stored ID for user {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Security Question");
        }
        log.trace("validateSecurityQuestion: successfully validated the securityQuestion for user {}", resetUser.getUsername());
        // validating that the stored hashed answer and given answer match
        log.trace("validateSecurityQuestion: validating the securityQuestionAnswer for user {}", resetUser.getUsername());
        if (!passwordService.validatePassword(forgotPasswordDTO.getSecurityQuestionAnswer(), resetUser.getSecurityQuestionAnswer())) {
            log.warn("validateSecurityQuestion: the security question given did not match with the hashed answer stored for username {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Security Question");
        }
        log.trace("validateSecurityQuestion: successfully validated the securityQuestionAnswer for user {}", resetUser.getUsername());
        log.info("validateSecurityQuestion: User {} successfully validated", forgotPasswordDTO.getUsername());
        return new ValidateResult(true, "Successfully validated");
    }

    /**
     * Resets the password for a user after validating the username, security question, and new password.
     * <p>
     * This method performs a series of validation checks: it ensures that the provided username exists,
     * validates the new password according to predefined rules, and checks that the new password is not the same
     * as the current password. If all checks pass, the user's password is updated and saved.
     * </p>
     *
     * @param forgotPasswordDTO the data transfer object containing the user's username and new password
     * @return a {@link ValidateResult} object containing the result of the password reset attempt.
     * If successful, it returns a success message; otherwise, it returns a failure message with the reason.
     */
    @Override
    public ValidateResult resetPassword(ForgotPasswordDTO forgotPasswordDTO) {
        log.info("resetPassword: resetting for user {}", forgotPasswordDTO.getUsername());
        // validating the username
        List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(forgotPasswordDTO.getUsername());
        if (foundUsers.isEmpty()) {
            log.warn("resetPassword: user {} does not exist", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Username");
        }
        // make sure not multiple users
        if (foundUsers.size() > 1) {
            log.warn("resetPassword: multiple users found with username {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Internal Username Error");
        }
        User user = foundUsers.get(0);
        // validating password
        if (!isValidPassword(forgotPasswordDTO.getPassword())) {
            log.warn("resetPassword: Invalid new Password for username {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Invalid Password");
        }
        // make sure old and new passwords are different
        final String oldHash = user.getPassword();
        if (passwordService.validatePassword(forgotPasswordDTO.getPassword(), oldHash)) {
            log.info("resetPassword: New password is same as old for user: {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "New Password is the same as old");
        }
        // set the password with the new password
        user.setPasswordHash(passwordService.hashPassword(forgotPasswordDTO.getPassword()));
        // make sure the new password if validated
        if (!passwordService.validatePassword(forgotPasswordDTO.getPassword(), user.getPassword())) {
            // if the new password doesn't match, then give it the old hash
            user.setPasswordHash(oldHash);
            log.info("resetPassword: New hashed password doesn't match new password for user, resetting to old password: {}", forgotPasswordDTO.getUsername());
            return new ValidateResult(false, "Error with new password");
        }
        // if all tests pass, its good and save changes
        loginRepo.save(user);
        return new ValidateResult(true, "Successfully reset password");
    }

    /**
     * Checks if any User in the database has an ID that matches the one given in the parameter.
     *
     * @param id - the ID to check for in the database
     * @return a {@link ValidateResult} object containing the result of the password reset attempt.
     * If successful, valid = true and a success message; otherwise, it returns valid = false and failure
     * message with the reason.
     */
    @Override
    public ValidateResult existsByID(Integer id) {
        log.trace("existsByID: existing user with id {}", id);
        if (id == null) {
            log.warn("existsByID: id is null");
            return new ValidateResult(false, "Invalid ID");
        }
        if (loginRepo.existsById(id)) {
            log.info("existsByID: existing user with id {}", id);
            return new ValidateResult(true, "User exists");
        }
        log.warn("existsByID: no user with id {}", id);
        return new ValidateResult(false, "User does not exist");
    }
}
