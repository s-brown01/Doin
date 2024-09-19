package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.LoginDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/login")
public class LoginController {
  static final Logger log = LoggerFactory.getLogger(LoginController.class);

  /**
   * Handles POST requests to the /api/login endpoint.
   * 
   * @param login A DTO object containing username and password fields.
   * @return A token or success message (e.g., "token").
   */
  @PostMapping()
  public String loginPost(LoginDTO login) {
    System.out.println(login.getUsername());
    return "{}";
  }

}
