package edu.carroll.doin_backend.web.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class will handle all hashed password generation and validating. All
 * methods and parameters in here are static so that all methods can be used by
 * every class in the application.
 * <BR/>
 * Structure for this class and how to implement BCrypt was influenced by
 * ChatGPT and <a href=
 * "https://auth0.com/blog/hashing-in-action-understanding-bcrypt/#How-does--bcrypt">
 * autho0.com </a>
 */
public class PasswordHandler {

  private static final BCryptPasswordEncoder hasher = new BCryptPasswordEncoder();

  // using static methods so we can reuse the same encoder for all classes

  /**
   * This method will hash the rawPassword parameter using the
   * BCryptPasswordEncoder and then return the hashed result.
   * 
   * @param rawPassword - the unhashed password that will be hashed and salted.
   * @return - the hashed and salted version of the password.
   */
  public static String hashPassword(String rawPassword) {
    return hasher.encode(rawPassword);
  }

  /**
   * This method will verify if the rawPassword parameter matches the
   * hashedPassword answer given using BCrypt's matches method, which already
   * takes salting into account.
   * 
   * @param rawPassword    - the unhashed password to compate
   * @param hashedPassword - the stored (hashed and salted) password to compare to
   * @return ture if the two passwords match, false otherwise
   */
  public static boolean validatePassword(String rawPassword, String hashedPassword) {
    return hasher.matches(rawPassword, hashedPassword);
  }
}
