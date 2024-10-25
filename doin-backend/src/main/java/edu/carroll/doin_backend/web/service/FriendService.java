package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface FriendService {
    FriendshipDTO[] getFriendsOfFriends(String userUsername);
    FriendshipDTO getUser(String userUsername);

    boolean addFriend(String userUsername, String friendUsername);
    boolean removeFriend(String userUsername, String friendUsername);
    boolean blockUser(String userUsername, String blockUsername);
    boolean unblockUser(String userUsername, String blockUsername);
}
