package edu.carroll.doin_backend.web.security;

/**
 * Service interface for handling password operations.
 * <p>
 * This interface defines methods for hashing passwords and validating
 * passwords against their hashed counterparts.
 * </p>
 */
public interface PasswordService {

  /**
   * Hashes the given raw password.
   *
   * @param rawPassword the raw password to hash
   * @return the hashed password
   */
  String hashPassword(String rawPassword);

  /**
   * Validates the given raw password against a hashed password.
   *
   * @param rawPassword  the raw password to validate
   * @param hashedPassword the hashed password to compare against
   * @return true if the raw password matches the hashed password; false otherwise
   */
  boolean validatePassword(String rawPassword, String hashedPassword);
}