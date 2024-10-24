package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface FriendService {
    public FriendshipDTO[] getFriendsOfFriends(String username);
    public boolean addFriend(String username1, String username2);
    public boolean removeFriend(String username1, String username2);
    public FriendshipDTO getFriend(String username);
}
