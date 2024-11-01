package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.FriendService;
import edu.carroll.doin_backend.web.service.FriendServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api/friends")
public class FriendController {
    private static final Logger log = LoggerFactory.getLogger(FriendController.class);

    private final FriendService friendService;

    private final TokenService tokenService;

    public FriendController(FriendService friendService, TokenService tokenService) {
        this.friendService = friendService;
        this.tokenService = tokenService;
    }

    @GetMapping()
    public ResponseEntity<Set<FriendshipDTO>> getFriendsOfFriends(@RequestHeader("Authorization") String authHeader) {
        log.trace("getFriendsOfFriends: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("getFriendsOfFriends: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        final String username = tokenResult.getMessage();
        if (!validateUsername(username)) {
            log.warn("getFriendsOfFriends: invalid username");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getFriendsOfFriends: username {} validated", username);
        Set<FriendshipDTO> friends = friendService.getFriendsOfFriends(username);
        log.trace("getFriendsOfFriends: username {} returned {} friends", username, friends.size());
        return ResponseEntity.ok(friends);
    }

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
        if (!validateUsername(userUsername)) {
            log.warn("getUserByUsername: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getUserByUsername: username {} validated", userUsername);
        // validate the friend username
        if (!validateUsername(otherUsername)) {
            log.warn("getUserByUsername: - Invalid friend username {}", otherUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getUserByUsername: username {} validated", otherUsername);
        Set<FriendshipDTO> newFriend = friendService.getUser(userUsername, otherUsername);
        log.info("getUserByUsername: username {} returned {} friends", otherUsername, newFriend.size());
        return ResponseEntity.ok(newFriend);
    }

    @GetMapping("/get-friends")
    public ResponseEntity<Set<FriendshipDTO>> getFriends(@RequestHeader("Authorization") String authHeader) {
        log.trace("getFriends: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("getFriends: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        final String userUsername = tokenResult.getMessage();
        if (!validateUsername(userUsername)) {
            log.warn("getFriends: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getFriends: username {} validated", userUsername);
        Set<FriendshipDTO> friends = friendService.getFriends(userUsername);
        log.trace("getFriends: username {} returned {} friends", userUsername, friends.size());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friend-requests")
    public ResponseEntity<Set<FriendshipDTO>> getFriendRequests(@RequestHeader("Authorization") String authHeader) {
        log.trace("getFriendRequests: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("getFriendRequests: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        final String userUsername = tokenResult.getMessage();
        if (!validateUsername(userUsername)) {
            log.warn("getFriendRequests: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashSet<>());
        }
        log.trace("getFriendRequests: username {} validated", userUsername);
        Set<FriendshipDTO> requests = friendService.getFriendRequests(userUsername);
        log.info("getFriendRequests: username {} has {} requests", userUsername, requests.size());
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/add/{friendUsername}")
    public ResponseEntity<Boolean> addFriend(@PathVariable String friendUsername, @RequestHeader("Authorization") String authHeader) {
        log.trace("addFriend: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("addFriend: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        final String userUsername = tokenResult.getMessage();
        if (!validateUsername(userUsername)) {
            log.warn("addFriend: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("addFriend: user username {} validated", userUsername);
        // validate the friendUsername
        if (!validateUsername(friendUsername)) {
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

    @PostMapping("/confirm/{friendUsername}")
    public ResponseEntity<Boolean> confirmFriend(@PathVariable String friendUsername, @RequestHeader("Authorization") String authHeader){
        log.trace("confirmFriend: validating authHeader, extracting jwtToken and username");
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("confirmFriend: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        final String userUsername = tokenResult.getMessage();
        if (!validateUsername(userUsername)) {
            log.warn("confirmFriend: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("confirmFriend: user username {} validated", userUsername);
        // validate the friendUsername
        if (!validateUsername(friendUsername)) {
            log.error("confirmFriend: - Invalid friend username {}", friendUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("confirmFriend: friend username {} validated", friendUsername);

        ValidateResult result = friendService.confirmFriend(userUsername, friendUsername);
        if (!result.isValid()) {
            log.info("confirmFriend: confirming friend {} was unsuccessful for user {}", friendUsername, userUsername);
            return ResponseEntity.ok(true);
        }
        log.info("confirmFriend: confirming friend {} was successful for user {}", friendUsername, userUsername);
        return ResponseEntity.ok(false);
    }

    @DeleteMapping("/remove/{friendUsername}")
    public ResponseEntity<Boolean> removeFriend(@PathVariable String friendUsername, @RequestHeader("Authorization") String authHeader) {
        ValidateResult tokenResult = validateTokenAndGetUsername(authHeader);
        if (!tokenResult.isValid()) {
            log.warn("removeFriend: invalid jwtToken or authHeader");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        final String userUsername = tokenResult.getMessage();
        if (!validateUsername(userUsername)) {
            log.warn("removeFriend: invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        log.trace("removeFriend: user username {} validated", userUsername);
        if (!validateUsername(friendUsername)) {
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
        if (!validateUsername(username)) {
            log.error("validateTokenAndGetUsername: - invalid username");
            return new ValidateResult(false, null);
        }
        log.info("validateTokenAndGetUsername: username {} validated", username);
        return new ValidateResult(true, username);
    }

    private boolean validateUsername(String username) {
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
