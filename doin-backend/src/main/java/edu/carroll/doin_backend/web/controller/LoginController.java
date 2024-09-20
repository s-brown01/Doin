package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.service.LoginService;
import edu.carroll.doin_backend.web.dto.LoginDTO;
import edu.carroll.doin_backend.security.JwtUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;

@RestController
public class LoginController {
  static final Logger log = LoggerFactory.getLogger(LoginController.class);

  private final JwtUtil jwtUtil;
  private final LoginService loginService;

  public LoginController(JwtUtil jwtUtil, LoginService loginService) {
    this.jwtUtil = jwtUtil;
    this.loginService = loginService;
  }

  /**
   * Handles GET requests to the /login endpoint.
   * 
   * @param model Spring model for adding attributes to the view.
   * @return A view name (e.g., "loginGet") to be resolved.
   */
  @GetMapping("/login")
  public String loginGet(Model model) {
    log.info("LOGIN GETTING");
    return "loginGet";
  }

  /**
   * Handles POST requests to the /api/login endpoint.
   * 
   * @param login A DTO object containing username and password fields.
   * @return A token or success message (e.g., "token").
   */
  @PostMapping("api/login")
  public String loginPost(LoginDTO login) {
    System.out.println(login.getUsername());
    boolean isValidUser = loginService.validateUser(login.getUsername(), login.getPassword());

    if (isValidUser) {
      final String token = jwtUtil.generateToken(login.getUsername());
      return token; // Return the JWT token
    } else {
      return null;
    }
  }

}
