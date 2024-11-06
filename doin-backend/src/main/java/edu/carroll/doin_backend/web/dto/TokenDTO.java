package edu.carroll.doin_backend.web.dto;

/**
 * Data Transfer Object (DTO) for transporting a JWT token between the backend
 * and the frontend, particularly after a successful login.
 * <br>
 * This class is used to encapsulate the token string that is generated during
 * authentication.
 * <br>
 * The class is implemented as a record, which automatically provides constructors
 * and getters.
 */
public class TokenDTO {

    private final String token;

    /**
     * Constructs a TokenDTO with the provided JWT token string.
     *
     * @param token the JWT token string to be transported.
     */
    public TokenDTO(String token) {
        this.token = token;
    }

    /**
     * Default constructor for TokenDTO. Sets the token to null.
     */
    public TokenDTO() {
        this.token = null;
    }

    /**
     * Gets the JWT token stored in this DTO.
     *
     * @return the JWT token string.
     */
    public String getToken() {
        return token;
    }

}
