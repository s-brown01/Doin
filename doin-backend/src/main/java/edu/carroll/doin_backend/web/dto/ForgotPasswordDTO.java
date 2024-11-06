package edu.carroll.doin_backend.web.dto;

/**
 * DTO for handling the forgot password functionality.
 * It contains the necessary information to identify the user
 * and validate the user's password recovery process.
 */
public class ForgotPasswordDTO {

    private final String username;
    private final String securityQuestionValue;
    private final String securityQuestionAnswer;
    private final String password;
    private String hashedPassword;

    /**
     * Constructs a ForgotPasswordDTO with the provided details.
     *
     * @param username the username of the user requesting password recovery.
     * @param securityQuestionValue the value of the security question for the user.
     * @param securityQuestionAnswer the answer to the security question.
     * @param password the new password that the user wishes to set.
     */
    public ForgotPasswordDTO(String username, String securityQuestionValue, String securityQuestionAnswer, String password) {
        this.username = username;
        this.securityQuestionValue = securityQuestionValue;
        this.securityQuestionAnswer = securityQuestionAnswer;
        this.password = password;
    }

    /**
     * Gets the username associated with this password recovery request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the security question value associated with the user.
     *
     * @return the security question value.
     */
    public String getSecurityQuestionValue() {
        return securityQuestionValue;
    }

    /**
     * Gets the answer to the security question.
     *
     * @return the security question answer.
     */
    public String getSecurityQuestionAnswer() {
        return securityQuestionAnswer;
    }

    /**
     * Gets the password that the user wants to set.
     *
     * @return the new password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the hashed password (after hashing the original password).
     *
     * @return the hashed password.
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Sets the hashed password value.
     *
     * @param hashedPassword the hashed version of the password.
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
