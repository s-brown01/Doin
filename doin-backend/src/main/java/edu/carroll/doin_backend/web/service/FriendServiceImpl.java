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
     * @param userUsername the username of the person to get mutual friends for.
     * @return A set of users who are friends of the user's friends, or an empty set if none found.
     */
    @Override
    public FriendshipDTO[] getFriendsOfFriends(String userUsername) {
        log.trace("getFriendsOfFriends: getting the friends of friends for username {}", userUsername);
        List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(userUsername);
        // if the username wasn't found, return an empty list
        if (foundUsers == null || foundUsers.isEmpty()) {
            log.warn("getFriendsOfFriends: no users found for username {}", userUsername);
            return new FriendshipDTO[0];
        }
        // if more than 1 user was found, return an empty list
        if (foundUsers.size() > 1) {
            log.warn("getFriendsOfFriends: more than one user found for username {}", userUsername);
            return new FriendshipDTO[0];
        }

        // get the user that was found
        User initialUser = foundUsers.get(0);
        // get the friends of friends
        log.trace("getFriendsOfFriends: getting friends from friendsRepository for username {}", userUsername);
        try {
            Set<FriendshipDTO> friends = friendRepo.findFriendsOfFriends(initialUser);
            log.trace("getFriendsOfFriends: found {} friends for username {}", friends.size(), userUsername);
            // if less than 5 friends of friends were found, populate the rest with random users
            // > 5 is ok, but make sure to have some suggestions
            if (friends.size() < 5) {
                log.trace("getFriendsOfFriends: no friends-of-friends found for username {}, fetching random users", userUsername);
                Set<FriendshipDTO> randomUsers = friendRepo.findRandomUsersExcept(initialUser, 5 - friends.size());
                // adding randomUsers to the friends Set
                friends.addAll(randomUsers);
            }
            log.trace("FriendController: converting set to a FriendshipDTO[] and returning it for user: {}", userUsername);
            return friends.toArray(new FriendshipDTO[0]);
        } catch (Exception e) {
            // if there are any Exceptions, return an empty Set
            log.warn("getFriendsOfFriends: error while getting friends of friends for username {}", userUsername);
            return new FriendshipDTO[0];
        }
    }

    @Override
    public boolean addFriend(String userUsername, String friendUsername) {
        return false;
    }

    @Override
    public boolean removeFriend(String userUsername, String friendUsername) {
        return false;
    }

    @Override
    public FriendshipDTO getFriend(String friendUsername) {
        return null;
    }
}
