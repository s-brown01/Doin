package edu.carroll.doin_backend.web.security;

import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    String generateToken(String username);

    boolean validateToken(String token);

    String getUsername(String token);
}
