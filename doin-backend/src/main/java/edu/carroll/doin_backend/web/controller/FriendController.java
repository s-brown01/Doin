package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller for handling friend-related API requests. This class provides endpoints
 *      for managing friends, retrieving friend lists, friend requests, and
 *      handling actions like adding, confirming, and removing friends.
 * Each method performs validation of the JWT token and user credentials before executing actions.
 */
@RestController
@RequestMapping("api/friends")
public class FriendController {
    /**
     * A {@link Logger} for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(FriendController.class);

    /**
     * A {@link FriendService} to use functions relating to the Friendship repository
     */
    private final FriendService friendService;

    /**
     * A {@link TokenService} for functions relating to JWT-Tokens
     */
    private final TokenService tokenService;


    /**
     * Constructor for a new FriendController
     *
     * @param friendService The service responsible for friend operations.
     * @param tokenService The service responsible for token validation.
     */
    public FriendController(FriendService friendService, TokenService tokenService) {
        this.friendService = friendService;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves the set of "friends of friends" for the authenticated user.
     * Ensures that no null values are returned and logs the process.
     *
     * @param authHeader The authorization header containing the JWT token.
     * @return A {@link ResponseEntity} containing a {@link Set} of friends
     *          of the user's friends, or an empty set if authentication fails or no
     *          friends are found.
     */
    @GetMapping()
    public ResponseEntity<Set<FriendshipDTO>> getFriendsOfFriends(@RequestHeader("Authorization") String authHeader) {
        log.trace("getFriendsOfFriends: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("getFriendsOfFriends: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        final String username = tokenResult.getMessage();
        if (!isValidUsername(username)) {
            log.warn("getFriendsOfFriends: invalid username");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getFriendsOfFriends: username {} validated", username);
        Set<FriendshipDTO> friends = friendService.getFriendsOfFriends(username);
        log.trace("getFriendsOfFriends: username {} returned {} friends", username, friends.size());
        return ResponseEntity.ok(friends);
    }

    /**
     * Retrieves a user by their specific username.
     *
     * @param authHeader The authorization header containing the JWT token.
     * @param otherUsername The username of the user to be searched for.
     * @return A {@link ResponseEntity} containing a {@link Set} of {@link FriendshipDTO} representing
     *          the found users, or an empty set if authentication fails or no friends are found.
     */
    @GetMapping("/{otherUsername}")
    public ResponseEntity<Set<FriendshipDTO>> getUserByUsername(@RequestHeader("Authorization") String authHeader, @PathVariable String otherUsername) {
        log.trace("getUserByUsername: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("getUserByUsername: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        final String userUsername = tokenResult.getMessage();
        // validating user username
        if (!isValidUsername(userUsername)) {
            log.warn("getUserByUsername: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getUserByUsername: username {} validated", userUsername);
        // validate the friend username
        if (!isValidUsername(otherUsername)) {
            log.warn("getUserByUsername: - Invalid friend username {}", otherUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getUserByUsername: username {} validated", otherUsername);
        Set<FriendshipDTO> newFriend = friendService.getUser(userUsername, otherUsername);
        log.info("getUserByUsername: username {} returned {} friends", otherUsername, newFriend.size());
        return ResponseEntity.ok(newFriend);
    }

    /**
     * Retrieves the user's friends. The user is based on the username in the JWT-Token.
     *
     * @param authHeader The authorization header containing the JWT token.
     * @return A {@link ResponseEntity} containing a {@link Set} of {@link FriendshipDTO} representing,
     *          the user's friends or an empty set if authentication fails or no friends are found.
     */
    @GetMapping("/get-friends")
    public ResponseEntity<Set<FriendshipDTO>> getFriends(@RequestHeader("Authorization") String authHeader) {
        log.trace("getFriends: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("getFriends: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        final String userUsername = tokenResult.getMessage();
        if (!isValidUsername(userUsername)) {
            log.warn("getFriends: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getFriends: username {} validated", userUsername);
        Set<FriendshipDTO> friends = friendService.getFriends(userUsername);
        log.trace("getFriends: username {} returned {} friends", userUsername, friends.size());
        return ResponseEntity.ok(friends);
    }

    /**
     * Retrieves the user's incoming friend requests. The user is based on the username in the JWT-Token.
     *
     * @param authHeader The authorization header containing the JWT token.
     * @return A {@link ResponseEntity} containing a {@link Set} of {@link FriendshipDTO} representing
     *          the incoming friend requests or an empty set if authentication fails or no incoming
     *          friend requests are found.
     */
    @GetMapping("/friend-requests")
    public ResponseEntity<Set<FriendshipDTO>> getFriendRequests(@RequestHeader("Authorization") String authHeader) {
        log.trace("getFriendRequests: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("getFriendRequests: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        final String userUsername = tokenResult.getMessage();
        if (!isValidUsername(userUsername)) {
            log.warn("getFriendRequests: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getFriendRequests: username {} validated", userUsername);
        Set<FriendshipDTO> requests = friendService.getFriendRequests(userUsername);
        log.info("getFriendRequests: username {} has {} requests", userUsername, requests.size());
        return ResponseEntity.ok(requests);
    }


    /**
     * Adds a friend to the user's friend list. The user is based on the username in the JWT-Token.
     *
     * @param friendUsername The username of the friend to add.
     * @param authHeader The authorization header containing the JWT token.
     * @return A {@link ResponseEntity} containing {@code true} if the friend was added,
     * or {@code false} if the operation failed or authentication is invalid.
     */
    @PostMapping("/add/{friendUsername}")
    public ResponseEntity<Boolean> addFriend(@PathVariable String friendUsername, @RequestHeader("Authorization") String authHeader) {
        log.trace("addFriend: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("addFriend: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        final String userUsername = tokenResult.getMessage();
        if (!isValidUsername(userUsername)) {
            log.warn("addFriend: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("addFriend: user username {} validated", userUsername);
        // validate the friendUsername
        if (!isValidUsername(friendUsername)) {
            log.error("addFriend: Invalid friend username {}", friendUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("addFriend: friend username {} validated", friendUsername);
        ValidateResult result = friendService.addFriend(userUsername, friendUsername);
        if (result.isValid()) {
            log.info("addFriend: adding friend {} was successful for user {}", friendUsername, userUsername);
            return ResponseEntity.ok(true);
        } else {
            log.info("addFriend: adding friend {} was unsuccessful for user {} because {}", friendUsername, userUsername, result.getMessage());
            return ResponseEntity.ok(false);
        }

    }

    /**
     * Confirms a pending friend request for the user. The user is based on the username in the JWT-Token.
     *
     * @param friendUsername The username of the friend to confirm.
     * @param authHeader The authorization header containing the JWT token.
     * @return A {@link ResponseEntity} containing {@code true} if the friend request was confirmed,
     * or {@code false} if the operation failed or authentication is invalid.
     */
    @PostMapping("/confirm/{friendUsername}")
    public ResponseEntity<Boolean> confirmFriend(@PathVariable String friendUsername, @RequestHeader("Authorization") String authHeader){
        log.trace("confirmFriend: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("confirmFriend: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        final String userUsername = tokenResult.getMessage();
        if (!isValidUsername(userUsername)) {
            log.warn("confirmFriend: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("confirmFriend: user username {} validated", userUsername);
        // validate the friendUsername
        if (!isValidUsername(friendUsername)) {
            log.error("confirmFriend: - Invalid friend username {}", friendUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("confirmFriend: friend username {} validated", friendUsername);

        ValidateResult result = friendService.confirmFriend(userUsername, friendUsername);
        if (result.isValid()) {
            log.debug("confirmFriend: confirming friend {} was successful for user {}", friendUsername, userUsername);
            return ResponseEntity.ok(true);
        }
        log.info("confirmFriend: confirming friend {} was unsuccessful for user {}", friendUsername, userUsername);
        return ResponseEntity.ok(false);
    }
    /**
     * Removes a friend from the user's friend list. The user is based on the username in the JWT-Token.
     *
     * @param friendUsername The username of the friend to remove.
     * @param authHeader The authorization header containing the JWT token.
     * @return A {@link ResponseEntity} containing {@code true} if the friendship was removed,
     * or {@code false} if the operation failed or authentication is invalid.
     */
    @DeleteMapping("/remove/{friendUsername}")
    public ResponseEntity<Boolean> removeFriend(@PathVariable String friendUsername, @RequestHeader("Authorization") String authHeader) {
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("removeFriend: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        final String userUsername = tokenResult.getMessage();
        if (!isValidUsername(userUsername)) {
            log.warn("removeFriend: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("removeFriend: user username {} validated", userUsername);
        if (!isValidUsername(friendUsername)) {
            log.error("removeFriend: - Invalid friend username {}", friendUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("removeFriend: friend username {} validated", friendUsername);

        ValidateResult result = friendService.removeFriend(userUsername, friendUsername);
        if (!result.isValid()) {
            log.info("removeFriend: removing friend {} was unsuccessful for user {}", friendUsername, userUsername);
            return ResponseEntity.ok(false);
        }
        log.info("removeFriend: removing friend {} was successful for user {}", friendUsername, userUsername);
        return ResponseEntity.ok(true);
    }

    /**
     * Validates the JWT token from the Authorization header and extracts the associated username.
     * Checks if the token exists, has the correct "Bearer" prefix, and if it is valid.
     * Logs relevant messages based on validation status and extracts the username if valid.
     *
     * @param header The Authorization header containing the JWT token, expected to be prefixed by "Bearer ".
     * @return A {@link ValidateResult} object containing a flag indicating validity and the extracted username
     * if the token is valid; otherwise, returns an invalid result with a null username.
     */
    private ValidateResult validateTokenAndGetUsername(String header) {
        if (header == null || header.isBlank() ||  !header.startsWith("Bearer ")) {
            log.error("getFriendsOfFriends: Missing or invalid Authorization header");
            return new ValidateResult(false, null);
        }
        // remove "Bearer " from header
        final String jwtToken = header.substring(7);
        log.trace("validateTokenAndGetUsername: retrieved jwt token from header");
        if (!tokenService.validateToken(jwtToken)) {
            log.error("validateTokenAndGetUsername: - invalid jwtToken");
            return new ValidateResult(false, null);
        }
        final String username = tokenService.getUsername(jwtToken);
        if (!isValidUsername(username)) {
            log.error("validateTokenAndGetUsername: - invalid username");
            return new ValidateResult(false, null);
        }
        log.info("validateTokenAndGetUsername: username {} validated", username);
        return new ValidateResult(true, username);
    }

    /**
     * Checks if the provided username is valid.
     * A username is considered valid if it is non-null, non-blank,
     * and contains only alphanumeric characters and underscores.
     *
     * @param username The username to validate.
     * @return {@code true} if the username is valid, {@code false} otherwise.
     */
    private boolean isValidUsername(String username) {
        if (username == null || username.isBlank()) {
            log.error("validateTokenAndGetUsername: - null or empty username");
            return false;
        }
        // make sure that the username only contains characters from the regex sequence
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            log.warn("validateTokenAndGetUsername: - username {} has unexpected characters", username);
            return false;
        }
        return true;
    }
}
