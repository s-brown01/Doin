package edu.carroll.doin_backend.web.service;

import org.springframework.stereotype.Service;

@Service
public interface RegisterService {
  public boolean createNewUser(String username, String password);
}
