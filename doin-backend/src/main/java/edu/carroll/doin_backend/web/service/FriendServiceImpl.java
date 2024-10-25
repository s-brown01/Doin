package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.model.Friendship;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.FriendRepository;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the FriendService interface that handles operations related to friends and friendships.
 * This service provides functionalities to retrieve friends of friends, add or remove friends, and get friendship details.
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

    /**
     * A JPA Repository that connects to the User model.
     */
    private final LoginRepository loginRepo;

    /**
     * Constructor to initialize FriendServiceImpl with the necessary repositories.
     *
     * @param friendRepository The repository for managing Friendship entities.
     * @param loginRepository  The repository for managing User entities.
     */
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
        log.trace("getFriendsOfFriends: validating username {}", userUsername);
        if (!validUsername(userUsername)) {
            log.warn("getFriendsOfFriends: invalid username {}", userUsername);
            return new FriendshipDTO[0];
        }
        // get the user that was found with the valid username
        User initialUser = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);
        // get the friends of friends
        log.trace("getFriendsOfFriends: getting friends from friendsRepository for username {}", userUsername);
        try {
            Set<FriendshipDTO> friends = friendRepo.findFriendsOfFriends(initialUser);
            log.trace("getFriendsOfFriends: found {} friends for username {}", friends.size(), userUsername);

            // if less than 5 friends of friends were found, populate the rest with random users
            // > 5 is ok, but make sure to have some suggestions
            if (friends.size() < 5) {
                log.trace("getFriendsOfFriends: no friends-of-friends found for username {}, fetching random users", userUsername);
                Set<FriendshipDTO> randomUsers = getRandomUsers(initialUser,5 - friends.size());
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

    /**
     * Retrieves a set of random users who are not already friends with the given user.
     * The number of random users returned is specified by amtOfUsers.
     *
     * @param user        The user for whom to find random non-friends.
     * @param amtOfUsers  The number of random users to retrieve.
     * @return A set of FriendshipDTO representing random users who are not friends.
     */
    private Set<FriendshipDTO> getRandomUsers(User user, int amtOfUsers) {
        // create an empty set to store all random users
        Set<FriendshipDTO> randomUsers = new HashSet<>();

        // a list of all users in the repository
        List<User> allUsers = loginRepo.findAll();

        // Create a Set for faster lookup of existing friends
        Set<User> allFriends = new HashSet<>(friendRepo.findByUser(user).stream()
                .map(Friendship::getFriend)
                .toList());
        // shuffle the list to create random selection of users
        Collections.shuffle(allUsers);

        for (User randomUser : allUsers) {
            // if we have collected enough random users, break the for loop
            if (randomUsers.size() >= amtOfUsers) {
                break;
            }
            // if the randomUser is not already a friend, add them
            if (!allFriends.contains(randomUser) &&
                !randomUser.equals(user)) {
                randomUsers.add(new FriendshipDTO(randomUser.getId(), randomUser.getUsername(), FriendshipStatus.NOTADDED, randomUser.getProfilePicture()));
            }
        }

        return randomUsers;
    }

    @Override
    public FriendshipDTO[] getUser(String otherUsername) {
        // if an exact match return just that user
        // if no exact match return an array with similar usernames
        return null;
    }

    /**
     * Adds a friend for the user specified by the username.
     *
     * <p>
     * This method validates the user and friend usernames, checks if a user is trying to friend themselves,
     * checks friend has not blocked user, and verifies that a connection
     * does not already exist between them. If all validations pass, a new friendship is created.
     * </p>
     *
     * @param userUsername  the username of the user who wants to add a friend.
     * @param friendUsername the username of the friend to be added.
     * @return a {@link ValidateResult} object containing the status of the operation.
     *         The status is true if user successfully friended 'friendUsername'; false if unsuccessful,
     *         along with a message indicating the result of the operation.
     */
    @Override
    public ValidateResult addFriend(String userUsername, String friendUsername) {
        log.trace("addFriend: adding friend {} for user {}", friendUsername, userUsername);
        log.trace("addFriend: validating user username {}", userUsername);
        if (!validUsername(userUsername)) {
            log.warn("addFriend: invalid user username {}", userUsername);
            return new ValidateResult(false, "invalid user username");
        }
        log.trace("addFriend: validating friend username {}", friendUsername);
        if (!validUsername(friendUsername)) {
            log.warn("addFriend: invalid friend username {}", friendUsername);
            return new ValidateResult(false, "invalid friend username");
        }
        User user = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);
        User friend = loginRepo.findByUsernameIgnoreCase(friendUsername).get(0);

        log.trace("addFriend: checking user {} and friend {} are not the same", userUsername, friendUsername);
        if (user.equals(friend)) {
            log.info("addFriend: user {} and friend {} are the same", user, friend);
            return new ValidateResult(false, "user and friend are the same");
        }

        log.trace("addFriend: checking the user {} is not blocked by friend {}", userUsername, friendUsername);
        // check if the friend has a connection with user
        if (friendRepo.existsFriendshipByUserAndFriend(friend, user)) {
            // get the connection/Friendship
            FriendshipStatus friendToUserStatus = friendRepo.findByUserAndFriend(friend, user).getStatus();
            // make sure it is not PENDING already, confirm friendship
            if (friendToUserStatus.equals(FriendshipStatus.PENDING)) {
                log.info("addFriend: user {} and friend {} is already pending; now confirming friends", user, friendUsername);

            }

            // check if the user is blocked
            if (friendToUser.getStatus().equals(FriendshipStatus.BLOCKED)) {
                log.info("addFriend: friend {} has blocked user {}", friendUsername, userUsername);
                return new ValidateResult(false, "friend has blocked user");
            }
            // if not blocked, continue on
        }

        // make sure there is not already a Friendship from user to friend
        log.trace("addFriend: checking user {} and friend {} are not already friends", userUsername, friendUsername);
        if (friendRepo.existsFriendshipByUserAndFriend(user, friend)) {
            // if there is make sure it is not not-added (just in case
            FriendshipStatus currentStatus = friendRepo.findByUserAndFriend(user, friend).getStatus();
            if (!currentStatus.equals(FriendshipStatus.NOTADDED)) {
                log.warn("addFriend: there is already a friendship between user {} and friend {} with status {}", userUsername, friendUsername, currentStatus);
                return new ValidateResult(false, "there is already a friendship between user");
            }
        }
        log.trace("addFriend: creating user {} and friend {} friendship", userUsername, friendUsername);
        Friendship newFriendship = new Friendship(user, friend, FriendshipStatus.PENDING);
        friendRepo.save(newFriendship);

        return new ValidateResult(true, "user has friended friend!");
    }

    @Override
    public ValidateResult removeFriend(String userUsername, String friendUsername) {
        return new ValidateResult(false, "false for now");
    }

    @Override
    public ValidateResult confirmFriend(String userUsername, String friendUsername) {
        return new ValidateResult(false, "default response");
    }

    /**
     * A helper method that checks if a specific username is valid in the database. It checks that the username is in the database and that there is only 1 user with that username. Also, it catches any errors that may occur.
     *
     * @param username - the username to check against the database
     * @return true if the username is valid, false if not (not found, more than 1 user with that username, and any errors)
     */
    private boolean validUsername(String username) {
        try {
            List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(username);
            if (foundUsers == null || foundUsers.isEmpty()) {
                log.warn("validateUsername: no users found for username {}", username);
                return false;
            }
            if (foundUsers.size() > 1) {
                log.warn("validateUsername: more than one user found for username {}", username);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.warn("validateUsername: error while validating username {}", username);
            return false;
        }
    }
}
