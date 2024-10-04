package edu.carroll.doin_backend.web.security;

public interface PasswordService {

  String hashPassword(String rawPassword);

  boolean validatePassword(String rawPassword, String hashedPassword);

}
