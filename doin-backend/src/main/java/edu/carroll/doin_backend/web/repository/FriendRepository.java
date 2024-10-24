package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.model.Friendship;
import edu.carroll.doin_backend.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * Repository interface for managing {@link Friendship} entities and related operations.
 * Extends the JpaRepository to provide CRUD and custom query methods for Friendship.
 */
public interface FriendRepository extends JpaRepository<Friendship, Integer> {

    /**
     * Retrieves a set of FriendshipDTOs representing friends of the user's friends but are not directly friends
     * with the given user.
     *
     * <p>This function the custom query was generated with help from StackOverflow</p>
     * @param user The {@link User} for whom to find friends of friends.
     * @return A set of FriendshipDTOs who are friends of the user's friends but not direct friends of the user.
     */
    @Query("SELECT new edu.carroll.doin_backend.web.dto.FriendshipDTO(f2.friend.id, f2.friend.username, f2.status, f2.friend.profilePicture) " +
            "FROM Friendship f1 " +
            "JOIN Friendship f2 ON f1.friend = f2.user " +
            "WHERE f1.user = :user " +
            "AND f2.friend != :user " +
            "AND f2.friend NOT IN (SELECT f3.friend FROM Friendship f3 WHERE f3.user = :user)")
    Set<FriendshipDTO> findFriendsOfFriends(@Param("user") User user);

    /**
     * Retrieves a set of random users, excluding the given user and their direct friends.
     * This method is useful for suggesting new users to connect with when no friends-of-friends are found.
     *
     * <p>This query returns random users who are not directly friends with the specified user and excludes the user
     * themselves. The number of users retrieved can be limited by the 'limit' parameter.</p>
     *
     * <p>This function's custom query was generated with help from StackOverflow</p>
     *
     * @param currentUser The {@link User} to exclude from the results, along with their direct friends.
     * @param limit The maximum number of random users to retrieve.
     * @return A set of {@link FriendshipDTO} objects representing random users, excluding the current user and their friends.
     */
    @Query("SELECT new edu.carroll.doin_backend.web.dto.FriendshipDTO(u.id, u.username, null, u.profilePicture) " +
            "FROM User u " +
            "WHERE u != :currentUser " +
            "AND u NOT IN (SELECT f.friend FROM Friendship f WHERE f.user = :currentUser) " +
            "ORDER BY RAND()")
    Set<FriendshipDTO> findRandomUsersExcept(@Param("currentUser") User currentUser, @Param("limit") int limit);
}
