package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * The constructor of a LoginServiceImpl. It needs a LoginRepository and a PasswordService in order. The parameters in constructor allow Springboot to automatically inject dependencies.
     * @param loginRepo - the LoginRepository which holds all registered Users
     * @param passwordService - the PasswordService to verify user's password
     */
    public UserServiceImpl(LoginRepository loginRepo, PasswordService passwordService){
        this.loginRepo = loginRepo;
        this.passwordService = passwordService;
    }

//    @Override
    public boolean createNewUser(RegisterDTO registerDTO) {
        // HERE IS WHERE HASH
        return false;
    }

    /**
     * This method will ensure that the username inputted is in the database and
     * that the password matches the User with that username.
     *
     * @param username - the username the person logging in is using
     * @param password - the password the person logging in is using
     * @return true if the username and password are both valid; false if 1 or both
     *         are invalid.
     */
    @Override
    public boolean validateCredentials(String username, String password) {
        log.debug("validateCredentials: user '{}' attempting login", username);
        List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(username);

        // we expect 1 user found per username.
        // if we find less than 1 user...
        if (foundUsers.isEmpty()) {
            log.debug("validateCredentials: found less than 1 user (0 total)");
            return false;
        }

        // if we find more than 1 user...
        if (foundUsers.size() > 1) {
            log.debug("validateCredentials: found more than 1 user ({})", foundUsers.size());
            return false;
        }

        // made sure only 1 user, now grab it
        User user = foundUsers.get(0);

        // now validate the password using the Service's built in validator
        if (!passwordService.validatePassword(password, user.getHashedPassword())) {
            log.debug("validateCredentials: given password did not match with user's, {}, previously stored password", username);
            return false;
        }

        // if passed all checks, then finally return true
        log.info("validateCredentials: User {} successfully validated", username);
        return true;
    }
}
