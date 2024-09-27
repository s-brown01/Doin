package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.model.SecurityQuestion;

public class RegisterDTO {

    public RegisterDTO(String username, String password, String confirmPassword, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityQuestionString = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    private String username;
    private String password;
    private String securityQuestionString;
    private String securityAnswer;
    private Integer securityQuestionId;

    public Integer getSecurityQuestionId() {
        return securityQuestionId;
    }

    public void setSecurityQuestionId(Integer securityQuestionId) {
        this.securityQuestionId = securityQuestionId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public String getSecurityQuestionString() {
        return securityQuestionString;
    }

    public void clearData(){
        this.username = null;
        this.password = null;
        this.securityQuestionString = null;
        this.securityAnswer = null;
        this.securityQuestionId = null;
    }


}
