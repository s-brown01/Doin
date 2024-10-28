package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import edu.carroll.doin_backend.web.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
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
        // check that's not null and not blank (empty or just whitespace)
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(new HashSet<>());
        }
        log.info("FriendController: starting to get Friends-of-Friends for user: {}", username);
        Set<FriendshipDTO> friends = friendService.getFriendsOfFriends(username);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Set<FriendshipDTO>> getUserByUsername(@PathVariable String username){
        log.info("FriendController: starting to get User with username: {}", username);
        Set<FriendshipDTO> newFriend = friendService.getUser(username);
        return ResponseEntity.ok(newFriend);
    }

    @PostMapping("/add-friend")
    public ResponseEntity<Boolean> addFriend(@RequestBody UserDTO friendDTO, @RequestHeader("Username") String username){
        ValidateResult result = friendService.addFriend(username, friendDTO.getUsername());
        if (result.isValid()){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/confirm-friend")
    public ResponseEntity<Boolean> confirmFriend(@RequestBody UserDTO friendDTO, @RequestHeader("Username") String username){
        ValidateResult result = friendService.confirmFriend(username, friendDTO.getUsername());
        if (result.isValid()){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/remove-friend")
    public ResponseEntity<Boolean> removeFriend(@RequestBody UserDTO friendDTO, @RequestHeader("Username") String username) {
        ValidateResult result = friendService.removeFriend(username, friendDTO.getUsername());
        if (result.isValid()) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

}
