package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    public Set<UserDTO> getFriendsOfFriends(){
        return null;
    }

    @PostMapping("/{username}")
    public UserDTO[] getFriendByUsername(@PathVariable String username){
        return null;
    }

    @PostMapping("/add-friend")
    public UserDTO addFriend(@RequestBody UserDTO userDTO){
        return null;
    }

    @PostMapping("/remove-friend")
    public UserDTO removeFriend(@RequestBody UserDTO userDTO){
        return null;
    }
}
