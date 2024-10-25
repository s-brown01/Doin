package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
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
    public ResponseEntity<FriendshipDTO[]> getUserByUsername(@PathVariable String username){
        log.info("FriendController: starting to get User with username: {}", username);
        FriendshipDTO[] newFriend = friendService.getUser(username);
        return ResponseEntity.ok(newFriend);
    }

    @PostMapping("/add-friend")
    public ResponseEntity<Boolean> addFriend(@RequestBody UserDTO userDTO, @RequestHeader("Username") String username){
        ValidateResult result = friendService.addFriend(username, userDTO.getUsername());
        return null;
    }

    @PostMapping("/remove-friend")
    public ResponseEntity<Boolean> removeFriend(@RequestBody UserDTO userDTO, @RequestHeader("Username") String username) {
        ValidateResult result = friendService.removeFriend(username, userDTO.getUsername());
        return null;
    }

    @PostMapping("/block-user")
    public ResponseEntity<Boolean> blockUser(@RequestBody UserDTO userDTO, @RequestHeader("Username") String username){
        ValidateResult result = friendService.blockUser(userDTO.getUsername(), username);
        return null;
    }

    @PostMapping
    public ResponseEntity<Boolean> unblockUser(@RequestBody UserDTO userDTO, @RequestHeader("Username") String username){
        ValidateResult result = friendService.unblockUser(userDTO.getUsername(), username);
        return null;
    }
}
