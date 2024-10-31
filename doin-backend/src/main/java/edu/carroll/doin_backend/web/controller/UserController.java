package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.security.JwtTokenService;
import edu.carroll.doin_backend.web.service.UserService;
import org.apache.coyote.Request;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    public UserController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping()
    public ResponseEntity<UserDTO> getUser(@RequestParam(required = false) Integer id,
                                           @RequestParam(required = false) String username) {
        UserDTO user = userService.findUser(id, username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-profile-img")
    public boolean updateProfileImage(@RequestParam("file") MultipartFile file,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        String token = authHeader.substring(7); // Remove "Bearer " prefix

        String userName = jwtTokenService.getUsername(token);
        return userService.updateProfilePicture(userName, file);
    }
}
