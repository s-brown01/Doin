package edu.carroll.doin_backend.service;

public interface PasswordService {

  String hashPassword(String rawPassword);

  boolean validatePassword(String rawPassword, String hashedPassword);

}
