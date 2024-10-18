package edu.carroll.doin_backend.web.dto;

public class ForgotPasswordDTO {
    private final String username;
    private final String securityQuestionValue;
    private final String securityQuestionAnswer;
    private final String password;
    private String hashedPassword;

    public ForgotPasswordDTO(String username, String securityQuestionValue, String securityQuestionAnswer, String password) {
        this.username = username;
        this.securityQuestionValue = securityQuestionValue;
        this.securityQuestionAnswer = securityQuestionAnswer;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getSecurityQuestionValue() {
        return securityQuestionValue;
    }

    public String getSecurityQuestionAnswer() {
        return securityQuestionAnswer;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
