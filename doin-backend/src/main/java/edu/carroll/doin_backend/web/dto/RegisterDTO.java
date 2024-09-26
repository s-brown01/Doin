package edu.carroll.doin_backend.web.dto;

public class RegisterDTO {

    public RegisterDTO(String username, String password, Integer securityQuestionId, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityQuestionId = securityQuestionId;
        this.securityAnswer = securityAnswer;
    }

    private String username;
    private String password;
    private Integer securityQuestionId;
    private String securityAnswer;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
