package edu.carroll.doin_backend.web.dto;

/**
 * Data Transfer Object (DTO) for user registration.
 * Contains the necessary fields for registering a new user, including
 * username, password, security question, and answer.
 */
public class RegisterDTO {

    private String username;
    private String password;
    private String securityQuestionString;
    private String securityAnswer;
    private Integer securityQuestionId;

    /**
     * Constructs a RegisterDTO with the provided username, password,
     * security question, and security answer.
     *
     * @param username the username to be registered.
     * @param password the password to be registered.
     * @param securityQuestion the security question selected during registration.
     * @param securityAnswer the answer to the security question.
     */
    public RegisterDTO(String username, String password, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityQuestionString = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    /**
     * Gets the ID of the security question associated with this registration.
     *
     * @return the security question ID.
     */
    public Integer getSecurityQuestionId() {
        return securityQuestionId;
    }

    /**
     * Sets the ID of the security question associated with this registration.
     *
     * @param securityQuestionId the security question ID to set.
     */
    public void setSecurityQuestionId(Integer securityQuestionId) {
        this.securityQuestionId = securityQuestionId;
    }

    /**
     * Gets the username for this registration.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password for this registration.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the answer to the security question for this registration.
     *
     * @return the security answer.
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * Sets the answer to the security question for this registration.
     *
     * @param securityAnswer the security answer to set.
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    /**
     * Gets the string value of the security question for this registration.
     *
     * @return the security question string.
     */
    public String getSecurityQuestionString() {
        return securityQuestionString;
    }

    /**
     * Clears all sensitive data fields from this DTO, resetting the values
     * to null.
     */
    public void clearData() {
        this.username = null;
        this.password = null;
        this.securityQuestionString = null;
        this.securityAnswer = null;
        this.securityQuestionId = null;
    }
}
