package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<FriendshipDTO[]> getFriendsOfFriends(@RequestHeader("Username") String username) {
        log.info("FriendController: starting to get Friends-of-Friends for user: {}", username);
        FriendshipDTO[] friends = friendService.getFriendsOfFriends(username);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO[]> getFriendByUsername(@PathVariable String username){
        return null;
    }

    @PostMapping("/add-friend")
    public ResponseEntity<Boolean> addFriend(@RequestBody UserDTO userDTO){
        return null;
    }

    @PostMapping("/remove-friend")
    public ResponseEntity<Boolean> removeFriend(@RequestBody UserDTO userDTO){
        return null;
    }
}
