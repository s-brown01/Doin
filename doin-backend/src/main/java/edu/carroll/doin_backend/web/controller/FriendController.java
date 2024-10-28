package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import edu.carroll.doin_backend.web.service.FriendService;
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

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping()
    public ResponseEntity<Set<FriendshipDTO>> getFriendsOfFriends(@RequestHeader("Username") String username) {
        log.info("FriendController: starting to get Friends-of-Friends for user: {}", username);
        // validate the username
        if (!isValidUsername(username)) {
            log.error("getFriendsOfFriends: - Invalid username {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashSet<>());
        }
        log.trace("getFriendsOfFriends: username {} validated", username);
        Set<FriendshipDTO> friends = friendService.getFriendsOfFriends(username);
        log.trace("getFriendsOfFriends: username {} returned {} friends", username, friends.size());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{otherUsername}")
    public ResponseEntity<Set<FriendshipDTO>> getUserByUsername(@RequestHeader("Username") String userUsername, @PathVariable String otherUsername) {
        log.info("FriendController: starting to get friend with username: {} for user {}", otherUsername, userUsername);
        // validate the user username
        if (!isValidUsername(userUsername)) {
            log.error("getUserByUsername: - Invalid user username {}", userUsername);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashSet<>());
        }
        log.trace("getUserByUsername: username {} validated", userUsername);

        // validate the friend username
        if (!isValidUsername(otherUsername)) {
            log.warn("getUserByUsername: - Invalid friend username {}", otherUsername);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashSet<>());
        }
        log.trace("getUserByUsername: username {} validated", otherUsername);
        Set<FriendshipDTO> newFriend = friendService.getUser(userUsername, otherUsername);
        log.trace("getUserByUsername: username {} returned {} friends", otherUsername, newFriend.size());
        return ResponseEntity.ok(newFriend);
    }

    @GetMapping("/friend-requests")
    public ResponseEntity<Set<FriendshipDTO>> getFriendRequests(@RequestHeader("Username") String username) {
        log.info("FriendController: starting to get friend requests for user: {}", username);
        if (!isValidUsername(username)) {
            log.error("getFriendRequests: - Invalid username {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashSet<>());
        }
        log.trace("getFriendRequests: username {} validated", username);
        Set<FriendshipDTO> requests = friendService.getFriendRequests(username);
        log.trace("getFriendRequests: username {} has {} requests", username, requests.size());
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/add-friend")
    public ResponseEntity<Boolean> addFriend(@RequestBody UserDTO friendDTO, @RequestHeader("Username") String username) {
        log.info("FriendController: starting to save user: {}", username);
        // validate the username
        if (!isValidUsername(username)) {
            log.error("addFriend: - Invalid user username {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        // validate the friendUsername
        if (!isValidUsername(friendDTO.getUsername())) {
            log.error("addFriend: - Invalid friend username {}", friendDTO.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        log.trace("addFriend: user username {} and friend username {} validated", username, friendDTO.getUsername());
        ValidateResult result = friendService.addFriend(username, friendDTO.getUsername());
        if (result.isValid()) {
            log.debug("addFriend: adding friend {} was unsuccessful for user {}", friendDTO.getUsername(), username);
            return ResponseEntity.ok(true);
        }
        log.trace("addFriend: adding friend {} was successful for user {}", friendDTO.getUsername(), username);
        return ResponseEntity.ok(false);
    }

    @PostMapping("/confirm-friend")
    public ResponseEntity<Boolean> confirmFriend(@RequestBody UserDTO friendDTO, @RequestHeader("Username") String username){
        log.info("FriendController: starting to confirm friends between friend {} and user: {}", friendDTO.getUsername(), username);
        // validate the username
        if (!isValidUsername(username)) {
            log.error("confirmFriend: - Invalid user username {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        // validate the friendUsername
        if (!isValidUsername(friendDTO.getUsername())) {
            log.error("confirmFriend: - Invalid friend username {}", friendDTO.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        ValidateResult result = friendService.confirmFriend(username, friendDTO.getUsername());
        if (result.isValid()) {
            log.debug("confirmFriend: confirming friend {} was unsuccessful for user {}", friendDTO.getUsername(), username);
            return ResponseEntity.ok(true);
        }
        log.trace("confirmFriend: confirming friend {} was successful for user {}", friendDTO.getUsername(), username);
        return ResponseEntity.ok(false);
    }

    @PostMapping("/remove-friend")
    public ResponseEntity<Boolean> removeFriend(@RequestBody UserDTO friendDTO, @RequestHeader("Username") String username) {
        log.info("removeFriend: starting to remove friendship between friend {} and user: {}", friendDTO.getUsername(), username);
        // validate the username
        if (!isValidUsername(username)) {
            log.error("removeFriend: - Invalid user username {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        // validate the friendUsername
        if (!isValidUsername(friendDTO.getUsername())) {
            log.error("removeFriend: - Invalid friend username {}", friendDTO.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        ValidateResult result = friendService.removeFriend(username, friendDTO.getUsername());
        if (result.isValid()) {
            log.debug("removeFriend: removing friend {} was unsuccessful for user {}", friendDTO.getUsername(), username);
            return ResponseEntity.ok(true);
        }
        log.trace("removeFriend: removing friend {} was successful for user {}", friendDTO.getUsername(), username);
        return ResponseEntity.ok(false);
    }

    private boolean isValidUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        // make sure that the username only contains characters from the regex sequence
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return false;
        }
        return true;
    }

}
