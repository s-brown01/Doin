package edu.carroll.doin_backend.web.dto;

public class ForgotPasswordDTO {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getSecurityQuestionValue() {
        return securityQuestionValue;
    }

    public void setSecurityQuestionValue(String securityQuestionValue) {
        this.securityQuestionValue = securityQuestionValue;
    }

    public String getSecurityQuestionAnswer() {
        return securityQuestionAnswer;
    }

    public void setSecurityQuestionAnswer(String securityQuestionAnswer) {
        this.securityQuestionAnswer = securityQuestionAnswer;
    }

    private String username;
    private String securityQuestionValue;
    private String securityQuestionAnswer;
}
