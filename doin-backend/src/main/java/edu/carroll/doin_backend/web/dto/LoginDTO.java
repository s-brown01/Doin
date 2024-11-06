package edu.carroll.doin_backend.web.dto;

/**
 * Data Transfer Object (DTO) for handling user login information.
 * Contains the necessary fields for authenticating a user.
 */
public class LoginDTO {

    private final String username;
    private final String password;

    /**
     * Constructs a LoginDTO with the provided username and password.
     *
     * @param username the username of the user attempting to log in.
     * @param password the password of the user attempting to log in.
     */
    LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username associated with this login request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password associated with this login request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }
}
