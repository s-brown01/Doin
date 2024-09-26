package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.LoginDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.security.JwtUtil;

import edu.carroll.doin_backend.web.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;

@RestController
@RequestMapping("api")
public class LoginController {
  private static final Logger log = LoggerFactory.getLogger(LoginController.class);

  private final JwtUtil jwtUtil;
  private final UserService userService;

  public LoginController(JwtUtil jwtUtil, UserService userService) {
    this.jwtUtil = jwtUtil;
    this.userService = userService;
  }

  /**
   * Handles POST requests to the /api/login endpoint.
   * 
   * @param login A DTO object containing username and password fields.
   * @return ResponseEntity with either the JWT-Token or a HttpStatus.Unauthorized
   */
  @PostMapping("/login")
  public ResponseEntity<String> loginUser(LoginDTO login) {
    log.info("LoginController: user {} attemptig login", login.getUsername());
    boolean isValidUser = userService.validateCredentials(login.getUsername(), login.getPassword());

    // if the user is validated by our loginService...
    if (isValidUser) {
      final String token = jwtUtil.generateToken(login.getUsername());
      // Return the JWT token
      return ResponseEntity.ok(token);
    // if NOT valid...
    } else {
      // return that the username or password is invalid, no more specific than that to not reveal info
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(RegisterDTO register) {
    log.info("LoginController: user {} attemptig register", register.getUsername());
    boolean registered = userService.createNewUser(register);

    // if the user is validated by our loginService...
    if (registered) {
      return ResponseEntity.ok("{}");
    } else {
      // return that the username or password is invalid, no more specific than that to not reveal info
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }
  }

}
