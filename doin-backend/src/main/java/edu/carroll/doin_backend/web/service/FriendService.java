package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface FriendService {
    public FriendshipDTO[] getFriendsOfFriends(String userUsername);
    public boolean addFriend(String userUsername, String friendUsername);
    public boolean removeFriend(String userUsername, String friendUsername);
    public FriendshipDTO getFriend(String friendUsername);
}
