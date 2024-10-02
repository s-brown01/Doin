package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.LoginDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.TokenDTO;

import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api")
public class LoginController {
  private static final Logger log = LoggerFactory.getLogger(LoginController.class);

  private final TokenService tokenService;
  private final UserService userService;
    private final LoginRepository loginRepository;

    public LoginController(TokenService tokenService, UserService userService, LoginRepository loginRepository) {
    this.tokenService = tokenService;
    this.userService = userService;
        this.loginRepository = loginRepository;
    }

  /**
   * Handles POST requests to the /api/login endpoint.
   * 
   * @param login A DTO object containing username and password fields.
   * @return ResponseEntity with either the JWT-Token or a HttpStatus.Unauthorized
   */
  @PostMapping("/login")
  public ResponseEntity<TokenDTO> loginPost(@RequestBody LoginDTO login) {
    log.info("LoginController: user {} attemptig login", login.getUsername());
    boolean isValidUser = userService.validateCredentials(login.getUsername(), login.getPassword());

    // if the user is validated by our loginService...
    if (isValidUser) {
      // generate new token and store it in DTO
      final String token = tokenService.generateToken(login.getUsername());
      final TokenDTO tokenDTO = new TokenDTO(token);
      // Return the DTO
      return ResponseEntity.ok(tokenDTO);
    // if NOT valid...
    } else {
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
            return ResponseEntity.ok("{}");
        } else {
            // return that the username or password is invalid, no more specific than that to not reveal info
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody TokenDTO tokenDTO) {
      log.info("LoginController: validating token");
      log.debug("Toke" );

        if (tokenDTO.getToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

      boolean valid = userService.validateToken(tokenDTO);

      if (valid) {
          return ResponseEntity.ok("{}");
      } else {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
      }

    }

}
