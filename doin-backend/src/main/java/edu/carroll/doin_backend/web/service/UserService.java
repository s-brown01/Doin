package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface that defines methods for user-related operations such as creating a new user,
 * validating credentials, managing profile pictures, and handling password resets.
 * <p>
 * The implementation of this interface should handle the business logic related to user account management,
 * including security validations and profile management.
 * </p>
 */
public interface UserService {
    /**
     * Creates a new user in the system if the provided data is valid.
     * <p>
     * The username must be unique and meet the necessary criteria (e.g., length, format).
     * If the username is valid and the registration data is correct, a new user is created.
     * </p>
     *
     * @param registerDTO the data transfer object containing the necessary information to create a new user,
     *                    such as username, password, email, etc.
     * @return true if the new user was successfully created, false otherwise.
     */
    boolean createNewUser(RegisterDTO registerDTO);
    /**
     * Validates the provided login credentials.
     * <p>
     * This method checks if the username exists in the database and if the associated password is correct.
     * </p>
     *
     * @param username the username of the user attempting to log in.
     * @param password the password provided by the user.
     * @return true if both the username and password are valid; false if either is invalid.
     */
    boolean validateCredentials(String username, String password);
    /**
     * Retrieves the user details based on the given user ID or username.
     * <p>
     * This method fetches user information such as the username, email, profile picture, etc.
     * </p>
     *
     * @param id       the unique identifier of the user (optional).
     * @param username the username of the user (optional).
     * @return a {@link UserDTO} containing the user information; may return null if no user is found.
     */
    UserDTO findUser(Integer id, String username);
    /**
     * Updates the user's profile picture.
     * <p>
     * This method allows the user to upload a new profile picture, which will replace the existing one.
     * The profile picture is stored as a file, and the user's profile is updated accordingly.
     * </p>
     *
     * @param userId the ID of the user whose profile picture is to be updated.
     * @param file   the new profile picture file to be uploaded.
     * @return true if the profile picture was successfully updated, false otherwise.
     */
    boolean updateProfilePicture(String userId, MultipartFile file);
    /**
     * Validates the user's security question and answer during the password recovery process.
     * <p>
     * This method checks the provided security question and answer against the stored values for the user.
     * </p>
     *
     * @param forgotPasswordDTO the data transfer object containing the username, security question, and answer.
     * @return a {@link ValidateResult} indicating whether the security question and answer are correct.
     */
    ValidateResult validateSecurityQuestion(ForgotPasswordDTO forgotPasswordDTO);
    /**
     * Resets the user's password based on the provided security question answer.
     * <p>
     * If the security question and answer are validated successfully, this method allows the user to reset
     * their password.
     * </p>
     *
     * @param forgotPasswordDTO the data transfer object containing the new password and the necessary user details.
     * @return a {@link ValidateResult} indicating whether the password reset was successful.
     */
    ValidateResult resetPassword(ForgotPasswordDTO forgotPasswordDTO);
}
