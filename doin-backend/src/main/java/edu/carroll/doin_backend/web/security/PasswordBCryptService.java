package edu.carroll.doin_backend.web.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling password hashing and validation using BCrypt.
 * <p>
 * This class uses {@link BCryptPasswordEncoder} to hash passwords with automatic salting.
 * It provides methods to hash raw passwords and validate hashed passwords against stored values.
 * </p>
 * <p>
 * The structure of this service was influenced by the blog post from Auth0 on BCrypt hashing: 
 * <a href="https://auth0.com/blog/hashing-in-action-understanding-bcrypt/#How-does--bcrypt">Auth0 Blog</a>
 * </p>
 */
@Service
public class PasswordBCryptService implements PasswordService {
  /**
   * The {@link BCryptPasswordEncoder} instance used to hash and validate passwords.
   * <p>
   * This field leverages BCrypt's secure hashing algorithm with automatic salting. It is used to
   * generate password hashes during user registration or password change and to verify raw
   * passwords against stored hashes during login or validation processes.
   * </p>
   */
  private final BCryptPasswordEncoder hasher = new BCryptPasswordEncoder();
  /**
   * Constructs a new {@link PasswordBCryptService} using {@link BCryptPasswordEncoder}.
   */
  public PasswordBCryptService() {
  }

  /**
   * Hashes the provided raw password using BCrypt and returns the salted hash.
   * <p>
   * This method generates a hashed password with an automatic salt using the BCrypt algorithm.
   * </p>
   *
   * @param rawPassword the raw password to be hashed.
   * @return the hashed and salted password.
   * @throws IllegalArgumentException if the {@code rawPassword} is {@code null}.
   */
  @Override
  public String hashPassword(String rawPassword) throws IllegalArgumentException {
    if (rawPassword == null) {
      throw new IllegalArgumentException("rawPassword parameter cannot be null");
    }
    return hasher.encode(rawPassword);
  }

  /**
   * Validates the raw password against a stored hashed password.
   * <p>
   * This method uses BCrypt's built-in {@link BCryptPasswordEncoder#matches(CharSequence, String)} method,
   * which compares the raw password with the stored hash and automatically considers salting.
   * </p>
   *
   * @param rawPassword    the raw password to validate.
   * @param hashedPassword the stored hashed password to compare against.
   * @return {@code true} if the raw password matches the stored hash, {@code false} otherwise.
   */
  @Override
  public boolean validatePassword(String rawPassword, String hashedPassword) {
    return hasher.matches(rawPassword, hashedPassword);
  }
}
