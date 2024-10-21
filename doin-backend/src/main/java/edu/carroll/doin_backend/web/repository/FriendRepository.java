package edu.carroll.doin_backend.web.repository;

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
     * Retrieves a set of users who are friends of the user's friends but are not directly friends
     * with the given user.
     * <p>
     * This query finds users who are connected to the given user through mutual friendships.
     * <BR>
     * This javadoc was created with help from chatGPT.
     *
     * @param user The {@link User} for whom to find friends of friends.
     * @return A set of users who are friends of the user's friends but not direct friends of the user.
     */
    @Query("SELECT f2.friend FROM Friendship f1 " +
            "JOIN Friendship f2 ON f1.friend = f2.user " +
            "WHERE f1.user = :user " +
            "AND f2.friend != :user " +
            "AND f2.friend NOT IN (SELECT f3.friend FROM Friendship f3 WHERE f3.user = :user)")
    Set<User> findFriendsOfFriends(@Param("user") User user);
}
