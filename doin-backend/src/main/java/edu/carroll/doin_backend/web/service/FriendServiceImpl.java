package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.FriendRepository;
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
    private final FriendRepository friendRepository;


    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
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
//        Set<FriendshipDTO> friends = friendRepository.findFriendsOfFriends(user);
//        log.info("getFriendsOfFriends: Fetching friends of friends for user: {}", user.getUsername());
//         if the JpaRepository returned a null, return an empty set instead so front-end doesn't break
//        if (friends == null) {
//            log.warn("GetFriendsOfFriends: null value found, returning empty set for user: {}", user.getUsername());
//            return new HashSet<>();
//        }
//        log.info("Found {} friends of friends for user: {}", friends.size(), user.getUsername());
//        return friends;
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
