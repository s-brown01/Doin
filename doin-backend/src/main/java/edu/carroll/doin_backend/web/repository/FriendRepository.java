package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.model.Friendship;
import edu.carroll.doin_backend.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friendship, Integer> {
    List<Friendship> findByUserAndStatus(User user, FriendshipStatus status);
    List<Friendship> findByFriendAndStatus(User user, FriendshipStatus status);

    @Query("SELECT f2.friend FROM Friendship f1 " +
            "JOIN Friendship f2 ON f1.friend = f2.user " +
            "WHERE f1.user = :user " +
            "AND f2.friend != :user " +
            "AND f2.friend NOT IN (SELECT f3.friend FROM Friendship f3 WHERE f3.user = :user)")
    List<User> findFriendsOfFriends(@Param("user") User user);
}
