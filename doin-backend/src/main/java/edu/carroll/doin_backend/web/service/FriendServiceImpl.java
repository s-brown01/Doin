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

import java.time.LocalDate;
import java.util.*;

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

    private final ImageService imageService;

    /**
     * Constructor to initialize FriendServiceImpl with the necessary repositories.
     *
     * @param friendRepository The repository for managing Friendship entities.
     * @param loginRepository  The repository for managing User entities.
     * @param imageService     The repository for managing Image entities
     */
    public FriendServiceImpl(FriendRepository friendRepository, LoginRepository loginRepository, ImageService imageService) {
        this.friendRepo = friendRepository;
        this.loginRepo = loginRepository;
        this.imageService = imageService;
    }

    /**
     * Retrieves the set of "friends of friends" for the given user.
     * Ensures that no null values are returned and logs the process.
     *
     * @param userUsername the username of the person to get mutual friends for.
     * @return A {@link Set} of users who are friends of the user's friends, or an empty set if none found.
     */
    @Override
    public Set<FriendshipDTO> getFriendsOfFriends(String userUsername) {
        log.trace("getFriendsOfFriends: getting the friends of friends for username {}", userUsername);
        log.trace("getFriendsOfFriends: validating username {}", userUsername);
        if (!isValidExistingUsername(userUsername)) {
            log.warn("getFriendsOfFriends: invalid username {}", userUsername);
            return new HashSet<>();
        }
        // get the user that was found with the valid username
        User initialUser = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);
        // get the friends of friends
        log.trace("getFriendsOfFriends: getting friends from friendsRepository for username {}", userUsername);
        try {
            Set<FriendshipDTO> friends = friendRepo.findFriendsOfFriends(initialUser);
            for (FriendshipDTO friend : friends) {
                // only if the image is null replace it
                if (friend.getProfilePic() == null) {
                    friend.setProfilePic(imageService.get(4L));
                }
            }
            log.trace("getFriendsOfFriends: found {} friends for username {}", friends.size(), userUsername);

            // if less than 5 friends of friends were found, populate the rest with random users
            // > 5 is ok, but make sure to have some suggestions
            if (friends.size() < 5) {
                log.trace("getFriendsOfFriends: no friends-of-friends found for username {}, fetching random users", userUsername);
                Set<FriendshipDTO> randomUsers = getRandomUsers(initialUser,5 - friends.size());
                // adding randomUsers to the friends Set
                friends.addAll(randomUsers);
            }

            log.trace("FriendController: returning friends for user: {}", userUsername);
            return friends;
        } catch (Exception e) {
            // if there are any Exceptions, return an empty Set
            log.warn("getFriendsOfFriends: error while getting friends of friends for username {}", userUsername);
            return new HashSet<>();
        }
    }

    /**
     * Retrieves a set of {@link FriendshipDTO} objects representing users whose usernames
     * match or are similar to the provided {@code usernameToFind} and who have a connection
     * with the current user, identified by {@code userUsername}.
     * <p>
     * The result includes users whose usernames exactly match, partially match, or
     * are similar to {@code usernameToFind} (case-insensitive), with a second lookup using
     * wildcards if an exact match search yields no results.
     * </p>
     *
     * @param userUsername   the username of the current user who is performing the search
     * @param usernameToFind the username to search for, which may be a partial or full username
     * @return a {@link Set} of {@link FriendshipDTO} objects representing the matched users.
     */
    @Override
    public Set<FriendshipDTO> getUser(String userUsername, String usernameToFind) {
        log.trace("getUser: validating user username {}", userUsername);
        if (!isValidExistingUsername(userUsername)) {
            log.warn("getUser: invalid user username {}", userUsername);
            return new HashSet<>();
        }
        log.trace("getUser: validating friend username {}", usernameToFind);
        if (!isValidExistingUsername(usernameToFind)) {
            log.warn("getUser: invalid friend username {}", usernameToFind);
            return new HashSet<>();
        }
        log.trace("getUser: getting the current User {}", userUsername);
        final User currentUser = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);

        final Set<FriendshipDTO> foundUsers = new HashSet<>();
        log.trace("getUser: finding all Users with a username containing {} and ignoring case", usernameToFind);
        List<User> listFriends = loginRepo.findByUsernameContainingIgnoreCase(usernameToFind);
        log.trace("getUser: found {} Users", listFriends.size());

        for (User friend : listFriends) {
            log.trace("getUser: adding friend {} to found users for user {}", friend.getId(), userUsername);
            FriendshipStatus currentStatus;
            // the friendship is either user -> friend, friend -> user, or NOTADDED
            if (friendRepo.existsFriendshipByUserAndFriend(currentUser, friend)) {
                currentStatus = statusBetween(currentUser, friend);
            } else {
                currentStatus = statusBetween(friend, currentUser);
            }
            foundUsers.add(new FriendshipDTO(friend.getId(), friend.getUsername(), currentStatus, friend.getProfilePicture()));
        }
        log.trace("getUser: found {} unique Users adding creating FriendshipDTOs and now returning the Set", foundUsers.size());
        return foundUsers;
    }

    @Override
    public Set<FriendshipDTO>
    getFriends(String userUsername) {
        log.trace("getFriends: validating user username {}", userUsername);
        if (!isValidExistingUsername(userUsername)) {
            log.warn("getFriends: invalid user username {}", userUsername);
            return new HashSet<>();
        }
        final User currentUser = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);
        // find all friendships both ways (currentUser = Friend and = User)
        log.trace("getFriends: getting friends for user {}", userUsername);
        Set<Friendship> foundFriends = friendRepo.findByFriendAndStatus(currentUser, FriendshipStatus.CONFIRMED);
        foundFriends.addAll(friendRepo.findByUserAndStatus(currentUser, FriendshipStatus.CONFIRMED));
        log.trace("getFriends: found {} friends for user {}", foundFriends.size(), userUsername);
        // convert Friendships into FriendshipDTO using the helper method
        return convertFriendshipIntoDTOS(currentUser, foundFriends);
    }

    @Override
    public Set<Integer> findFriendIdsByUserId(Integer userId, FriendshipStatus status) {
        log.trace("getting Friends: for user {}", userId);
        Set<Integer> foundFriends = friendRepo.findFriendIdsByUserId(userId, status);
        log.trace("getFriends: found {} friends for user {}", foundFriends.size(), userId);
        return foundFriends;
    }

    @Override
    public Set<FriendshipDTO> getFriendRequests(String userUsername) {
        log.debug("getFriendRequests: validating user username {}", userUsername);

        if (!isValidExistingUsername(userUsername)) {
            log.warn("getFriendRequests: invalid user username {}", userUsername);
            return Collections.emptySet();
        }

        log.debug("getFriendRequests: validated user username {}", userUsername);
        User currentUser = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);

        log.debug("getFriendRequests: fetching all incoming requests for user: {}", userUsername);
        Set<Friendship> incomingRequests = friendRepo.findByFriendAndStatus(currentUser, FriendshipStatus.PENDING);
        // convert Friendships into FriendshipDTO using the helper method
        Set<FriendshipDTO> requests = convertFriendshipIntoDTOS(currentUser, incomingRequests);
        log.debug("getFriendRequests: returning {} FriendshipDTOs for user {}", requests.size(), userUsername);
        return requests;
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
     *         The status of valid true if user successfully friended 'friendUsername'; valid is false if unsuccessful,
     *         along with a message indicating the result of the operation.
     */
    @Override
    public ValidateResult addFriend(String userUsername, String friendUsername) {
        log.trace("addFriend: adding friend {} for user {}", friendUsername, userUsername);
        // Validate user username
        if (!isValidExistingUsername(userUsername)) {
            log.warn("addFriend: invalid user username {}", userUsername);
            return new ValidateResult(false, "invalid user username");
        }
        // Validate friend username
        if (!isValidExistingUsername(friendUsername)) {
            log.warn("addFriend: invalid friend username {}", friendUsername);
            return new ValidateResult(false, "invalid friend username");
        }
        // Fetching user and friend from repository
        User user = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);
        User friend = loginRepo.findByUsernameIgnoreCase(friendUsername).get(0);

        if (user.equals(friend)) {
            log.info("addFriend: user {} cannot add themselves as a friend", userUsername);
            return new ValidateResult(false, "You cannot add yourself as a friend");
        }

        // Check friendship status
        FriendshipStatus friendToUserStatus = statusBetween(friend, user);
        FriendshipStatus currentStatus = statusBetween(user, friend);

        if (friendToUserStatus == FriendshipStatus.CONFIRMED || currentStatus == FriendshipStatus.CONFIRMED) {
            log.error("addFriend: user {} is already friends with {}", userUsername, friendUsername);
            return new ValidateResult(false, "You are already friends with " + friendUsername);
        }
        // if friend to user is already PENDING: confirm friendship
        if (friendToUserStatus == FriendshipStatus.PENDING) {
            log.debug("addFriend: friend {} has already sent a request to user {}", friendUsername, userUsername);
            log.trace("addFriend: setting Friendship.Status to CONFIRMED between friend {} and user {}", friendUsername, userUsername);
            Friendship friendship = friendRepo.findByUserAndFriend(friend, user);
            // if both add friendship is CONFIRMED
            friendship.setStatus(FriendshipStatus.CONFIRMED);
            // Set confirmedAt to current date/time
            friendship.setConfirmedAt(LocalDate.now().atTime(0, 0));
            friendRepo.save(friendship);
            log.trace("addFriend: CONFIRMED Friendship saved between user {} and friend {}", userUsername, friendUsername);
            return new ValidateResult(true, "You are now friends with " + friendUsername);
        }
        // if user to friend is already pending, don't make another friendship
        if (currentStatus == FriendshipStatus.PENDING) {
            log.warn("addFriend: user {} has already sent a request to friend {}", userUsername, friendUsername);
            return new ValidateResult(false, "You have already sent a request to " + friendUsername);
        }

        // Create new friendship
        Friendship newFriendship = new Friendship(user, friend, FriendshipStatus.PENDING);
        friendRepo.save(newFriendship);

        log.info("addFriend: user {} has sent a friend request to {}", userUsername, friendUsername);
        return new ValidateResult(true, userUsername + " has sent a friend request to " + friendUsername);
    }

    @Override
    public ValidateResult removeFriend(String userUsername, String friendUsername) {
        log.trace("removeFriend: removing friend {} for user {}", friendUsername, userUsername);
        log.trace("removeFriend: validating user username {}", userUsername);
        if (!isValidExistingUsername(userUsername)) {
            log.warn("removeFriend: invalid user username {}", userUsername);
            return new ValidateResult(false, "invalid user username");
        }
        log.trace("removeFriend: validating friend username {}", friendUsername);
        if (!isValidExistingUsername(friendUsername)) {
            log.warn("removeFriend: invalid friend username {}", friendUsername);
            return new ValidateResult(false, "invalid friend username");
        }
        User user = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);
        User friend = loginRepo.findByUsernameIgnoreCase(friendUsername).get(0);
        if (user.equals(friend)) {
            log.warn("removeFriend: user {} and friend {} are the same", userUsername, friendUsername);
            return new ValidateResult(false, "user and friend are the same: " + user.getUsername() + " and " + friend.getUsername());
        }
        // make sure it exists (either direction)
        Friendship currentFriendship;
        // check Friendship from user to friend
        if (friendRepo.existsFriendshipByUserAndFriend(user, friend)) {
            log.trace("removeFriend: user {} is friends with friend {}", userUsername, friendUsername);
            currentFriendship = friendRepo.findByUserAndFriend(user, friend);
        } else if (friendRepo.existsFriendshipByUserAndFriend(friend, user)) {
            log.trace("removeFriend: friend {} is friends with user {}", friendUsername, friendUsername);
            currentFriendship = friendRepo.findByUserAndFriend(friend, user);
        } else {
            log.warn("removeFriend: user {} and friend {} are not friends", userUsername, friendUsername);
            return new ValidateResult(false, "user and friend are not friends");
        }
        friendRepo.delete(currentFriendship);
        return new ValidateResult(true, "user " + userUsername + " and friend " + friendUsername + " are no longer friends");
    }

    @Override
    public ValidateResult confirmFriend(String userUsername, String friendUsername) {
        // the addFriend already handles what happens when 2 different users send each other friend requests, so use that method
        log.trace("confirmFriend: confirming friendship between user {} and friend {}", userUsername, friendUsername);
        log.trace("confirmFriend: sending request to addFriend for user {} and friend {}", userUsername, friendUsername);
        return addFriend(userUsername, friendUsername);
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
                if (randomUser.getProfilePicture() == null) {
                    randomUsers.add(new FriendshipDTO(randomUser.getId(), randomUser.getUsername(), FriendshipStatus.NOTADDED, randomUser.getProfilePicture()));
                } else {
                    randomUsers.add(new FriendshipDTO(randomUser.getId(), randomUser.getUsername(), FriendshipStatus.NOTADDED, randomUser.getProfilePicture()));
                }
            }
        }

        return randomUsers;
    }
    
    /**
     * A helper method that checks if a specific username is valid in the database. It checks that the username is in the database and that there is only 1 user with that username. Also, it catches any errors that may occur.
     *
     * @param username - the username to check against the database
     * @return true if the username is valid, false if not (not found, more than 1 user with that username, and any errors)
     */
    private boolean isValidExistingUsername(String username) {
        try {
            List<User> foundUsers = loginRepo.findByUsernameIgnoreCase(username);
            // make sure the user exists in the repo
            if (foundUsers == null || foundUsers.isEmpty()) {
                log.warn("validateUsername: no users found for username {}", username);
                return false;
            }
            // make sure that only 1 user with the username
            if (foundUsers.size() > 1) {
                log.warn("validateUsername: more than one user found for username {}", username);
                return false;
            }
            // should be 1 existing user with the username
            log.trace("validateUsername: only {} User found with username {}", foundUsers.size(), username);
            return true;
        } catch (Exception e) {
            log.error("validateUsername: error while validating username {} - {}", username, e.getStackTrace());
            return false;
        }
    }

    private FriendshipStatus statusBetween(User user, User friend) {
        log.debug("statusBetween: checking if there is a status between user {} and user {}", user, friend);
        // if there is no relationship return NOTADDED
        if (!friendRepo.existsFriendshipByUserAndFriend(user, friend)) {
            return FriendshipStatus.NOTADDED;
        }
        // there is a current friendship, return the current status
        return friendRepo.findByUserAndFriend(user, friend).getStatus();
    }

    private Set<FriendshipDTO> convertFriendshipIntoDTOS(User user, Set<Friendship> friends) {
        Set<FriendshipDTO> friendDTOs = new HashSet<>();
        for (Friendship friendship : friends) {
            User friend = friendship.getUser();
            FriendshipStatus status = statusBetween(friend, user);
            Long profilePicId = (friend.getProfilePicture() == null) ? 4L : friend.getProfilePicture().getId();
            friendDTOs.add(new FriendshipDTO(friend.getId(), friend.getUsername(), status, imageService.get(profilePicId)));
        }
        return friendDTOs;
    }
}
