package edu.carroll.doin_backend.web.security;

/**
 * Service interface for handling token operations.
 * <p>
 * This interface defines methods for generating and validating tokens,
 * as well as extracting user information from tokens.
 * </p>
 */
public interface TokenService {

    /**
     * Generates a token for the given username and user ID.
     *
     * @param username the username for which to generate a token
     * @param id       the user ID for which to generate a token
     * @return the generated token as a string
     */
    String generateToken(String username, Integer id);

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

    /**
     * Extracts the user ID from the given token.
     *
     * @param token the token from which to extract the user ID
     * @return the user ID associated with the token
     */
    Integer getUserId(String token);
}