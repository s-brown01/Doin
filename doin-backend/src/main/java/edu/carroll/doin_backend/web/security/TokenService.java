package edu.carroll.doin_backend.web.security;

/**
 * Service interface for handling token operations.
 * <p>
 * This interface defines methods for generating and validating tokens,
 * as well as extracting usernames from tokens.
 * </p>
 */
public interface TokenService {

    /**
     * Generates a token for the given username.
     *
     * @param username the username for which to generate a token
     * @return the generated token as a string
     */
    String generateToken(String username);

    /**
     * Validates the given token.
     *
     * @param token the token to validate
     * @return true if the token is valid; false otherwise
     */
    boolean validateToken(String token);

    /**
     * Extracts the username from the given token.
     *
     * @param token the token from which to extract the username
     * @return the username associated with the token
     */
    String getUsername(String token);

    String generatePasswordResetToken(String username);
}