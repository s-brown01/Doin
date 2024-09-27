package edu.carroll.doin_backend.web.dto;

public class LoginDTO {
    private final String username;
    private final String password;

    LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
