package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.model.SecurityQuestion;

public class RegisterDTO {

    public RegisterDTO(String username, String password, String confirmPassword, Integer securityQuestionId, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityQuestionId = securityQuestionId;
        this.securityAnswer = securityAnswer;
    }

    private String username;
    private final String password;
    private Integer securityQuestionId;
    private String securityAnswer;
    private String passwordHashed;

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getSecurityQuestionId() {
        return securityQuestionId;
    }

    public void setSecurityQuestionId(Integer securityQuestionId) {
        this.securityQuestionId = securityQuestionId;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}
