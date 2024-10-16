package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.LoginDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.TokenDTO;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class LoginController {
  private static final Logger log = LoggerFactory.getLogger(LoginController.class);

  private final TokenService tokenService;
  private final UserService userService;

    public LoginController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

  /**
   * Handles POST requests to the /api/login endpoint.
   * 
   * @param login A DTO object containing username and password fields.
   * @return ResponseEntity with either the JWT-Token or a HttpStatus.Unauthorized
   */
  @PostMapping("/login")
  public ResponseEntity<TokenDTO> loginPost(@RequestBody LoginDTO login) {
    log.info("LoginController: user {} attempting login", login.getUsername());
//    boolean isValidUser = userService.validateCredentials(login.getUsername(), login.getPassword());
    boolean isValidUser = false;
    try {
        isValidUser = userService.validateCredentials(login.getUsername(), login.getPassword());
    } catch (Exception e) {
        log.error("LoginController: user {} login errored {}", login.getUsername(), e.getStackTrace());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    log.info("LoginController: isValidUser = {}", isValidUser);
    String token;

    // if the user is validated by our loginService...
    if (isValidUser) {
        log.info("LoginController: user {} successfully logged in, generating JWT-Tokens", login.getUsername());
        // generate new token and store it in DTO
        token = tokenService.generateToken(login.getUsername());
        final TokenDTO tokenDTO = new TokenDTO(token);
        // Return the DTO
        return ResponseEntity.ok(tokenDTO);
    // if NOT valid...
    } else {
        token = tokenService.generateToken(login.getUsername());
        log.info("LoginController: failure {}", tokenService.getUsername(token));
        log.info("LoginController: user {} failed to log in", login.getUsername());
        // return that the username or password is invalid, no more specific than that to not reveal info
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
  }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO register) {
        log.info("LoginController: new user {} registering", register.getUsername());

        boolean registered = userService.createNewUser(register);

        // if the user is validated by our loginService...
        if (registered) {
            log.info("LoginController: user {} successfully registered", register.getUsername());
            return ResponseEntity.ok("{}");
        } else {
            log.warn("LoginController: user {} failed to register", register.getUsername());
            // return that the username or password is invalid, no more specific than that to not reveal info
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody TokenDTO tokenDTO) {
      log.info("LoginController: validating token");
      Map<String, Object> response = new HashMap<>();

      if (tokenDTO.getToken() == null) {
          log.debug("LoginController: NULL token");
          response.put("success", false);
          response.put("message", "No token found");
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
      }

      boolean valid = userService.validateToken(tokenDTO);

      if (valid) {
          log.info("LoginController: valid token for user {}", tokenService.getUsername(tokenDTO.getToken()));
          response.put("success", true);
          response.put("message", "Valid token");
          return ResponseEntity.ok(response);
      } else {
          log.warn("LoginController: invalid token for user {}", tokenService.getUsername(tokenDTO.getToken()));
          response.put("success", false);
          response.put("message", "Invalid token");
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
      }

    }

}
