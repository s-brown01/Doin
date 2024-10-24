package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.model.Friendship;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.FriendRepository;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the FriendService that handles operations related to friends and friendships.
 */
@Service
public class FriendServiceImpl implements FriendService {
    /**
     * A Logger to just for this class
     */
    private static final Logger log = LoggerFactory.getLogger(FriendServiceImpl.class);

    /**
     * A JPA Repository that connects to Friendship Model
     */
    private final FriendRepository friendRepo;
    private final LoginRepository loginRepo;


    public FriendServiceImpl(FriendRepository friendRepository, LoginRepository loginRepository) {
        this.friendRepo = friendRepository;
        this.loginRepo = loginRepository;
    }

    /**
     * Retrieves the set of "friends of friends" for the given user.
     * Ensures that no null values are returned and logs the process.
     *
     * @param username the username of the person to get mutual friends for.
     * @return A set of users who are friends of the user's friends, or an empty set if none found.
     */
    @Override
    public Set<FriendshipDTO> getFriendsOfFriends(String username) {
        log.trace("getFriendsOfFriends: getting the friends of friends for username {}", username);
        List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(username);
        // if the username wasn't found, return an empty list
        if (foundUsers == null || foundUsers.isEmpty()) {
            log.warn("getFriendsOfFriends: no users found for username {}", username);
            return new HashSet<>();
        }
        // if more than 1 user was found, return an empty list
        if (foundUsers.size() > 1) {
            log.warn("getFriendsOfFriends: more than one user found for username {}", username);
            return new HashSet<>();
        }
        // get the user that was found
        User initialUser = foundUsers.get(0);
        // get the friends of friends
        log.trace("getFriendsOfFriends: getting friends from friendsRepository for username {}", username);
        Set<FriendshipDTO> friends = friendRepo.findFriendsOfFriends(initialUser);
        log.trace("getFriendsOfFriends: found {} friends for username {}", friends.size(), username);
        return friends;
    }

    @Override
    public boolean addFriend(String username1, String username2) {
        return false;
    }

    @Override
    public boolean removeFriend(String username1, String username2) {
        return false;
    }

    @Override
    public FriendshipDTO getFriend(String username) {
        return null;
    }
}
