package edu.carroll.doin_backend.web.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * This class will handle all hashed password generation and validating. This
 * service uses BCrypt to hash passwords. Additionally, BCrypt has automatic
 * salting, which is all handled using the built-in methods.
 * <BR/>
 * Structure for this class and how to implement BCrypt was influenced by
 * ChatGPT and <a href=
 * "https://auth0.com/blog/hashing-in-action-understanding-bcrypt/#How-does--bcrypt">
 * autho0.com </a>
 */
@Service
public class PasswordBCryptService implements PasswordService {

  private final BCryptPasswordEncoder hasher = new BCryptPasswordEncoder();

  public PasswordBCryptService() {
  }

  /**
   * This method will hash the rawPassword parameter using the
   * BCryptPasswordEncoder and then return the hashed result.
   * 
   * @param rawPassword - the unhashed password that will be hashed and salted.
   * @return - the hashed and salted version of the password.
   */
  @Override
  public String hashPassword(String rawPassword) {
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
  @Override
  public boolean validatePassword(String rawPassword, String hashedPassword) {
    return hasher.matches(rawPassword, hashedPassword);
  }
}
