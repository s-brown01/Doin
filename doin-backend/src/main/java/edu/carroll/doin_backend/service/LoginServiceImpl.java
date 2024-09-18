package edu.carroll.doin_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import edu.carroll.doin_backend.repo.LoginRepository;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.utils.PasswordHandler;

public class LoginServiceImpl implements LoginService {

  // create a logger just for this class
  private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

  // create a LoginRepository to get the findByUsername method
  private final LoginRepository loginRepo;

  public LoginServiceImpl(LoginRepository loginRepo){
    this.loginRepo = loginRepo;
  }

  /**
   * This method will ensure that the username inputted is in the database and
   * that the password is valid.
   * 
   * @param username - the username the person logging in is using
   * @param password - the password the person logging in is using
   * @return true if the username and password are both valid; false if 1 or both
   *         are invalid.
   */
  @Override
  public boolean validateUser(String username, String password) {
    log.debug("validateUser: user '{}' attemping login", username);
    List<User> foundUsers = loginRepo.findbyUsernameIgnoreCase(username);
    
    // we expect 1 user found per username. 
    // if we find less than 1 user...
    if (foundUsers.size() < 1) {
      log.debug("validateUser: found less than 1 user ({})", foundUsers.size());
      return false;
    }

    // if we find more than 1 user...
    if (foundUsers.size() > 1) {
      log.debug("validateUser: found more than 1 user ({})", foundUsers.size());
      return false;
    }

    // made sure only 1 user, now grab it
    User user = foundUsers.get(0);

    if (!PasswordHandler.validatePassword(password, user.getHashedPassword())) {
      log.debug("validateUser: given password did not match with user's, {}, previously stored password", user);
      return false;
    }


    log.info("validateUser: User {} successfully validated", username);
    return true;
  }

}
