package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.security.JwtTokenService;
import edu.carroll.doin_backend.web.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * The {@code UserController} class handles HTTP requests related to user operations,
 * such as retrieving user details and updating profile images.
 * It serves as the entry point for user-related API endpoints.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    /**
     * Constructs a new {@code UserController} with the specified {@code UserService}
     * and {@code JwtTokenService}.
     *
     * @param userService     the service used to perform user operations.
     * @param jwtTokenService the service used to handle JWT token operations.
     */
    public UserController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Retrieves a user's details based on the provided user ID or username.
     *
     * @param id       the unique identifier of the user (optional).
     * @param username the username of the user (optional).
     * @return a {@link ResponseEntity} containing the {@link UserDTO} if found,
     * or a 404 Not Found response if the user does not exist.
     */
    @GetMapping()
    public ResponseEntity<UserDTO> getUser(@RequestParam(required = false) Integer id,
                                           @RequestParam(required = false) String username) {
        // Fetch user based on either ID or username
        UserDTO user = userService.findUser(id, username);

        // Return user details if found, otherwise return a 404 response
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates the profile image of the authenticated user.
     *
     * @param file       the {@link MultipartFile} representing the new profile image.
     * @param authHeader the authorization header containing the JWT token.
     * @return {@code true} if the profile image was successfully updated;
     * {@code false} otherwise.
     * @throws IllegalArgumentException if the file is null or the user ID cannot be extracted from the token.
     */
    @PutMapping("/update-profile-img")
    public boolean updateProfileImage(@RequestParam("file") MultipartFile file,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        // Extract user ID from the JWT token
        Integer userId = jwtTokenService.getUserId(authHeader);

        // Update the user's profile picture using the UserService
        return userService.updateProfilePicture(userId, file);
    }
}