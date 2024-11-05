package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.*;
import edu.carroll.doin_backend.web.dto.UserDTO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    /**
     * This method will create a new user, if it is possible. Username must be unique and must meet criteria.
     *
     * @param registerDTO - the DTO that contains the nessecary data to create a new user
     * @return - true if a new user has been created, false if not.
     */
    boolean createNewUser(RegisterDTO registerDTO);

    /**
     * This method will ensure that the username inputted is in the database and
     * that the password is valid.
     *
     * @param username - the username the person logging in is using
     * @param password - the password the person logging in is using
     * @return true if the username and password are both valid; false if 1 or both
     *         are invalid.
     */
    boolean validateCredentials(String username, String password);

    UserDTO findUser(Integer id, String username);

    boolean updateProfilePicture(String userId, MultipartFile file);

    ValidateResult validateSecurityQuestion(ForgotPasswordDTO forgotPasswordDTO);

    ValidateResult resetPassword(ForgotPasswordDTO forgotPasswordDTO);
}
