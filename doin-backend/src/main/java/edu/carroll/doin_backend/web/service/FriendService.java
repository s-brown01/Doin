package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.ValidateResult;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service interface for managing friendships and friend requests. Provides methods for
 * retrieving friends, handling friend requests, and managing friend relationships.
 */
public interface FriendService {
    /**
     * Retrieves the set of friend's friends for the specified user.
     *
     * @param userUsername The username of the user whose friends of friends are being retrieved.
     * @return A {@link Set} of {@link FriendshipDTO} objects representing friends of the user's friends.
     */
    Set<FriendshipDTO> getFriendsOfFriends(String userUsername);
    /**
     * Retrieves the friendship information between the specified user and a target user.
     *
     * @param userUsername The username of the user performing the search.
     * @param usernameToFind The username of the user to find.
     * @return A {@link Set} of {@link FriendshipDTO} objects representing the relationship, or an empty set if not found.
     */
    Set<FriendshipDTO> getUser(String userUsername, String usernameToFind);
    /**
     * Retrieves the list of friends for the specified user.
     *
     * @param userUsername The username of the user whose friends are being retrieved.
     * @return A {@link Set} of {@link FriendshipDTO} objects representing the user's friends.
     */
    Set<FriendshipDTO> getFriends(String userUsername);
    /**
     * Retrieves the list of friend requests for the specified user.
     *
     * @param userUsername The username of the user whose friend requests are being retrieved.
     * @return A {@link Set} of {@link FriendshipDTO} objects representing incoming friend requests for the user.
     */
    Set<FriendshipDTO> getFriendRequests(String userUsername);
    /**
     * Sends a friend request from one user to another.
     *
     * @param userUsername The username of the user sending the friend request.
     * @param friendUsername The username of the user receiving the friend request.
     * @return A {@link ValidateResult} indicating whether the friend request was successful.
     */
    ValidateResult addFriend(String userUsername, String friendUsername);
    /**
     * Removes a friend relationship between two users.
     *
     * @param userUsername The username of the user initiating the removal.
     * @param friendUsername The username of the friend being removed.
     * @return A {@link ValidateResult} indicating whether the friend removal was successful.
     */
    ValidateResult removeFriend(String userUsername, String friendUsername);
    /**
     * Confirms a friend request between two users, establishing a friend relationship.
     *
     * @param username The username of the user confirming the friend request.
     * @param username1 The username of the user whose friend request is being confirmed.
     * @return A {@link ValidateResult} indicating whether the friend confirmation was successful.
     */
    ValidateResult confirmFriend(String username, String username1);
}
