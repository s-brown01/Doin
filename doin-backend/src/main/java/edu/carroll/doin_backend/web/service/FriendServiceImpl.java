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
 * Implementation of the {@link FriendService} interface that manages friendship operations.
 * <p>
 * This service provides methods for:
 * <ul>
 *     <li>Retrieving a user's friends and their friends' details.</li>
 *     <li>Managing friend requests (sending, accepting, and removing friends).</li>
 *     <li>Checking and modifying the friendship status between users.</li>
 * </ul>
 * </p>
 * <p>
 * It also handles validation of usernames, ensuring users cannot friend themselves, and prevents duplicate or invalid requests.
 * </p>
 * <p>
 * All operations rely on data persistence through repositories such as {@code loginRepo} and {@code friendRepo}.
 * </p>
 */
@Service
public class FriendServiceImpl implements FriendService {
    /**
     * A {@link Logger} for logging messages and operations
     */
    private static final Logger log = LoggerFactory.getLogger(FriendServiceImpl.class);

    /**
     * A {@link FriendRepository} (JPA Repository) that connects to Friendship Model
     */
    private final FriendRepository friendRepo;

    /**
     * A {@link LoginRepository} (JPA Repository) that connects to the User model.
     */
    private final LoginRepository loginRepo;

    /**
     * Constructor to initialize FriendServiceImpl with the necessary repositories.
     *
     * @param friendRepository The repository for managing Friendship entities.
     * @param loginRepository  The repository for managing User entities.
     *                         =
     */
    public FriendServiceImpl(FriendRepository friendRepository, LoginRepository loginRepository) {
        this.friendRepo = friendRepository;
        this.loginRepo = loginRepository;
    }

    /**
     * Retrieves the set of mutual friends (friends of friends) for a given user.
     * <p>
     * The method first validates the username. If the username is invalid, it returns an empty set.
     * If valid, it fetches the user's direct friends, then retrieves and aggregates each friend's friends
     * (excluding the initial user from the result).
     * </p>
     * <p>
     * If an error occurs during processing (e.g., repository access failure), it logs a warning and returns an empty set.
     * </p>
     *
     * @param userUsername The username of the user whose mutual friends are being retrieved.
     * @return A {@link Set} of {@link FriendshipDTO} objects representing mutual friends (friends of friends),
     * or an empty set if the username is invalid or if an error occurs during processing.
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
            Set<FriendshipDTO> immediateFriends = getFriends(userUsername);
            Set<FriendshipDTO> mutualFriends = new HashSet<>();
            for (FriendshipDTO friend : immediateFriends) {
                Set<FriendshipDTO> friendFriends = getFriends(friend.getUsername());
                for (FriendshipDTO friendFriend : friendFriends) {
                    friendFriend.setStatus(FriendshipStatus.NOTADDED);
                    mutualFriends.add(friendFriend);
                }
            }
            mutualFriends.removeIf(friend -> friend.getUsername().equalsIgnoreCase(userUsername));
            log.info("getFriendsOfFriends: found {} friends for username {}", mutualFriends.size(), userUsername);

            log.info("FriendController: returning {} mutual friends for user: {}", mutualFriends.size(), userUsername);
            return mutualFriends;
        } catch (Exception e) {
            // if there are any Exceptions, return an empty Set
            log.warn("getFriendsOfFriends: error while getting friends of friends for username {}", userUsername);
            return new HashSet<>();
        }
    }

    /**
     * Finds users with usernames similar to {@code usernameToFind} and retrieves their
     * friendship status with the user identified by {@code userUsername}.
     * <p>
     * Searches for usernames matching {@code usernameToFind} (case-insensitive) and determines the
     * friendship status of each found user relative to {@code userUsername}. Possible statuses are:
     * <ul>
     *     <li>{@code user -> friend}</li>
     *     <li>{@code friend -> user}</li>
     *     <li>{@code NOTADDED} if no connection exists</li>
     * </ul>
     * Returns an empty set if usernames are invalid or no matches are found.
     * </p>
     *
     * @param userUsername   the username of the user performing the search.
     * @param usernameToFind the partial or full username to search for.
     * @return a {@link Set} of {@link FriendshipDTO} objects representing matching users
     * and their friendship status with {@code userUsername}; empty if none found.
     */
    @Override
    public Set<FriendshipDTO> getUser(String userUsername, String usernameToFind) {
        log.trace("getUser: validating user username {}", userUsername);
        if (!isValidExistingUsername(userUsername)) {
            log.warn("getUser: invalid user username {}", userUsername);
            return new HashSet<>();
        }
        log.trace("getUser: getting the current User {}", userUsername);
        final User currentUser = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);

        final Set<FriendshipDTO> foundUsers = new HashSet<>();
        log.trace("getUser: finding all Users with a username containing {} and ignoring case", usernameToFind);
        List<User> listFriends = loginRepo.findByUsernameLikeIgnoreCase("%" + usernameToFind + "%");
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
            // do not all the user to the friends list
            if (friend.getUsername().equalsIgnoreCase(userUsername)) {
                continue;
            }
            foundUsers.add(new FriendshipDTO(friend.getId(), friend.getUsername(), currentStatus, friend.getProfilePicture()));
        }
        log.trace("getUser: found {} unique Users adding creating FriendshipDTOs and now returning the Set", foundUsers.size());
        return foundUsers;
    }

    /**
     * Retrieves a set of confirmed friends for the specified user, identified by {@code userUsername}.
     * <p>
     * Finds all users with a confirmed friendship status where the specified user is either
     * the requester or recipient. Converts each friendship into a {@link FriendshipDTO} for return.
     * </p>
     * Returns an empty set if the username is invalid or no friends are found.
     *
     * @param userUsername the username of the user whose friends are being retrieved.
     * @return a {@link Set} of {@link FriendshipDTO} objects representing confirmed friends;
     * empty if none found or username is invalid.
     */
    @Override
    public Set<FriendshipDTO> getFriends(String userUsername) {
        log.trace("getFriends: validating user username {}", userUsername);
        if (!isValidExistingUsername(userUsername)) {
            log.warn("getFriends: invalid user username {}", userUsername);
            return new HashSet<>();
        }
        final User currentUser = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);
        // find all friendships both ways (currentUser = Friend and = User)
        log.trace("getFriends: getting friends for user {}", userUsername);
        // user -> friend
        Set<Friendship> foundFriends = friendRepo.findByFriendAndStatus(currentUser, FriendshipStatus.CONFIRMED);
        // friend -> user
        foundFriends.addAll(friendRepo.findByUserAndStatus(currentUser, FriendshipStatus.CONFIRMED));
        log.trace("getFriends: found {} friends for user {}", foundFriends.size(), userUsername);
        // convert Friendships into FriendshipDTO using the helper method
        return convertFriendshipIntoDTOS(currentUser, foundFriends);
    }

    /**
     * Retrieves the list of friends for a user based on the other User's ID. <BR>
     * It also sets the statuses of all friendships to the status between the current user and the found friends
     *
     * @param userUsername the username of the current user
     * @param otherID the ID of the User whose friends to get
     * @return A {@link Set} of {@link FriendshipDTO} objects representing the other user's friends
     */
    @Override
    public Set<FriendshipDTO> getFriendsOf(String userUsername, Integer otherID) {
        final Optional<User> otherUser = loginRepo.findById(otherID);
        // make sure the otherUser exists if it doesn't than return an empty set
        if (otherUser.isEmpty()) {
            log.warn("getFriendsOf: invalid user ID {}", otherID);
            return new HashSet<>();
        }
        // get the otherUser from the Optional above
        final String otherUsername = otherUser.get().getUsername();
        // get the current user based on the userUsername
        final User currentUser = loginRepo.findByUsernameIgnoreCase(userUsername).get(0);
        log.trace("getFriendsOf: getting friends for user {}", otherUsername);
        Set<FriendshipDTO> otherFriends = getFriends(otherUsername);
        // for each friend, check the status between the current user and that friend
        for (FriendshipDTO friend : otherFriends) {
            // if the friend is the current user, set the status appropriately
            if (friend.getUsername().equalsIgnoreCase(userUsername)) {
                friend.setStatus(FriendshipStatus.IS_SELF);
                continue;
            }
            // the friend's User model by the username
            final User tempFriend = loginRepo.findByUsernameIgnoreCase(friend.getUsername()).get(0);
            // if the friendship exists from User -> friend, use that
            if (friendRepo.existsFriendshipByUserAndFriend(currentUser, tempFriend)) {
                friend.setStatus(statusBetween(currentUser, tempFriend));
            } else {
                // if it doesn't exist, statusBetween already handles it
                friend.setStatus(statusBetween(tempFriend, currentUser));
            }
        }
        log.info("getFriendsOf: found {} friends for user {}", otherFriends.size(), otherUsername);
        return otherFriends;
    }

    @Override
    public Set<Integer> findFriendIdsByUserId(Integer userId, FriendshipStatus status) {
        log.trace("getting Friends: for user {}", userId);
        Set<Integer> foundFriends = friendRepo.findFriendIdsByUserId(userId, status);
        log.trace("findFriendIdsByUserId: found {} friends for user {}", foundFriends.size(), userId);
        return foundFriends;
    }

    /**
     * Retrieves a set of pending friend requests for the specified user, identified by {@code userUsername}.
     * <p>
     * Finds all incoming friend requests where the specified user is the recipient with a status of
     * {@code PENDING}. Converts each pending request into a {@link FriendshipDTO}.
     * </p>
     * Returns an empty set if the username is invalid or no requests are found.
     *
     * @param userUsername the username of the user for whom friend requests are being retrieved.
     * @return a {@link Set} of {@link FriendshipDTO} objects representing pending friend requests;
     * empty if none found or if the username is invalid.
     */
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
     * Sends a friend request from the user to the specified friend.
     *
     * <p>
     * This method performs several validations: it ensures both the user and the friend exist, checks that
     * the user is not attempting to friend themselves, verifies that no existing friendship exists, and
     * ensures the user hasn't already sent a pending request. If the friend has already sent a request,
     * the request is confirmed. If no connection exists, a new friendship request is created.
     * </p>
     *
     * @param userUsername   the username of the user sending the friend request.
     * @param friendUsername the username of the friend to whom the request is being sent.
     * @return a {@link ValidateResult} containing the status of the operation:
     * - {@code valid = true} if the friend request is successfully sent or confirmed,
     * - {@code valid = false} with a message indicating the reason for failure (e.g., already friends,
     * invalid username, or a pending request).
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

    /**
     * Removes the friendship between the specified user and friend.
     *
     * <p>
     * This method validates the user and friend usernames, checks that they are not the same person,
     * and ensures that a friendship exists between the two. If all validations pass, the friendship is
     * deleted, and the users are no longer considered friends.
     * </p>
     *
     * @param userUsername   the username of the user who wants to remove the friend.
     * @param friendUsername the username of the friend to be removed.
     * @return a {@link ValidateResult} object containing the result of the operation:
     * - {@code valid = true} if the friendship is successfully removed,
     * - {@code valid = false} with a message indicating why the removal failed
     * (e.g., the users are not friends, invalid usernames).
     */
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

    /**
     * Confirms a pending friend request between the specified user and their friend.
     *
     * <p>
     * This method checks if there is an existing pending friend request from the user to the friend.
     * If such a request exists, it confirms the friendship. Essentially, this method calls
     * {@link #addFriend(String, String)} to confirm the request.
     * </p>
     *
     * @param userUsername   the username of the user who is confirming the friend request.
     * @param friendUsername the username of the friend whose request is being confirmed.
     * @return a {@link ValidateResult} object containing the result of the operation:
     * - {@code valid = true} if the friendship is successfully confirmed,
     * - {@code valid = false} with a message indicating any failure reason (e.g., no pending request).
     */
    @Override
    public ValidateResult confirmFriend(String userUsername, String friendUsername) {
        // the addFriend already handles what happens when 2 different users send each other friend requests, so use that method
        log.info("confirmFriend: confirming friendship between user {} and friend {}", userUsername, friendUsername);
        log.trace("confirmFriend: sending request to addFriend for user {} and friend {}", userUsername, friendUsername);
        return addFriend(userUsername, friendUsername);
    }

    /**
     * Validates whether a given username exists in the database and is unique.
     * <p>
     * This method checks if the username exists in the database and ensures that
     * there is exactly one user with that username. It handles any exceptions
     * that may occur during the database query.
     * </p>
     *
     * @param username the username to check.
     * @return {@code true} if the username exists and is unique in the database;
     * {@code false} if the username is not found, is associated with more than
     * one user, or if an error occurs during validation.
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

    /**
     * Checks the current friendship status ({@link FriendshipStatus}) between two users.
     * <p>
     * This method checks whether a friendship exists between the given {@code user}
     * and {@code friend}, and returns the current status of their relationship.
     * If no relationship exists, the status returned is {@link FriendshipStatus#NOTADDED}.
     * </p>
     *
     * @param user   the first user in the relationship check.
     * @param friend the second user in the relationship check.
     * @return the {@link FriendshipStatus} between the two users,
     * or {@link FriendshipStatus#NOTADDED} if no friendship exists.
     */
    private FriendshipStatus statusBetween(User user, User friend) {
        log.debug("statusBetween: checking if there is a status between user {} and user {}", user, friend);
        // if there is no relationship return NOTADDED
        if (!friendRepo.existsFriendshipByUserAndFriend(user, friend)) {
            return FriendshipStatus.NOTADDED;
        }
        // there is a current friendship, return the current status
        return friendRepo.findByUserAndFriend(user, friend).getStatus();
    }

    /**
     * Converts a set of {@link Friendship} objects into a set of {@link FriendshipDTO} objects.
     * <p>
     * This method iterates over a set of {@link Friendship} entities, determines the status of
     * each friendship with respect to the specified {@code user}, and creates corresponding
     * {@link FriendshipDTO} objects for each friendship.
     * </p>
     *
     * @param user    the current user whose friendship status will be checked.
     * @param friends the set of {@link Friendship} objects to be converted.
     * @return a {@link Set} of {@link FriendshipDTO} objects, representing the converted friendships.
     */
    private Set<FriendshipDTO> convertFriendshipIntoDTOS(User user, Set<Friendship> friends) {
        Set<FriendshipDTO> friendDTOs = new HashSet<>();
        for (Friendship friendship : friends) {
            User friend;
            // is user = Friendship's 'friend' or are they the 'user'
            if (friendship.getFriend().equals(user)) {
                // the 'user' is NOT the current user, the current user is the 'friend'
                friend = friendship.getUser();
                FriendshipStatus status = statusBetween(friend, user);
                friendDTOs.add(new FriendshipDTO(friend.getId(), friend.getUsername(), status, friend.getProfilePicture()));
            } else {
                // the 'user' is the 'user', friend is 'friend'
                friend = friendship.getFriend();
                FriendshipStatus status = statusBetween(user, friend);
                friendDTOs.add(new FriendshipDTO(friend.getId(), friend.getUsername(), status, friend.getProfilePicture()));
            }
        }
        return friendDTOs;
    }
}
