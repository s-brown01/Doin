package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.model.Friendship;
import edu.carroll.doin_backend.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Repository interface for managing {@link Friendship} entities and related operations.
 * Extends the JpaRepository to provide CRUD and custom query methods for Friendship.
 */
public interface FriendRepository extends JpaRepository<Friendship, Integer> {

    /**
     * Retrieves a set of FriendshipDTOs representing friends of the user's friends but are not directly friends
     * with the given user with a {@link FriendshipStatus} of 'not added'.
     *
     * <p>This function the custom query was generated with help from StackOverflow</p>
     * @param user The {@link User} for whom to find friends of friends.
     * @return A set of FriendshipDTOs who are friends of the user's friends but not direct friends of the user.
     */
    @Query("SELECT new edu.carroll.doin_backend.web.dto.FriendshipDTO(f2.friend.id, f2.friend.username, edu.carroll.doin_backend.web.enums.FriendshipStatus.NOTADDED, f2.friend.profilePicture) " +
            "FROM Friendship f1 " +
            "JOIN Friendship f2 ON f1.friend = f2.user " +
            "WHERE f1.user = :user " +
            "AND f2.friend != :user " +
            "AND f2.friend NOT IN (SELECT f3.friend FROM Friendship f3 WHERE f3.user = :user)")
    Set<FriendshipDTO> findFriendsOfFriends(@Param("user") User user);

    Set<Friendship> findByFriendAndStatus(User user, FriendshipStatus status);

    Set<Friendship> findByUser(User user);

    boolean existsFriendshipByUserAndFriend(User user, User friend);

    Friendship findByUserAndFriend(User user, User friend);
}
