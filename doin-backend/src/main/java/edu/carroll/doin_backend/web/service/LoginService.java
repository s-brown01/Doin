package edu.carroll.doin_backend.web.service;

import org.springframework.stereotype.Service;

/**
 * The {@code LoginService} interface provides methods for user authentication and login validation.
 * It defines a method for validating users attempting to log in by checking their credentials (username and password).
 * Implementations of this interface should handle the necessary logic to verify valid usernames and passwords.
 */
@Service
public interface LoginService {

  /**
   * This method will ensure that the username inputted is in the database and
   * that the password is valid.
   * 
   * @param username - the username the person logging in is using
   * @param password - the password the person logging in is using
   * @return true if the username and password are both valid; false if 1 or both
   *         are invalid.
   */
  boolean validateUser(String username, String password);

}
