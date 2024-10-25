package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import org.springframework.stereotype.Service;


@Service
public interface FriendService {
    FriendshipDTO[] getFriendsOfFriends(String userUsername);
    FriendshipDTO[] getUser(String userUsername);

    ValidateResult addFriend(String userUsername, String friendUsername);
    ValidateResult removeFriend(String userUsername, String friendUsername);
    ValidateResult blockUser(String userUsername, String blockUsername);
    ValidateResult unblockUser(String userUsername, String blockUsername);
}
