package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface FriendService {
    Set<FriendshipDTO> getFriendsOfFriends(String userUsername);
    Set<FriendshipDTO> getUser(String userUsername, String usernameToFind);
    Set<FriendshipDTO> getFriendRequests(String userUsername);

    ValidateResult addFriend(String userUsername, String friendUsername);
    ValidateResult removeFriend(String userUsername, String friendUsername);

    ValidateResult confirmFriend(String username, String username1);
}
