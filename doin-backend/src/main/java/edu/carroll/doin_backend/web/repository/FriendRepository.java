package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.model.Friendship;
import edu.carroll.doin_backend.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FriendRepository extends JpaRepository<Friendship, Integer> {
    @Query("SELECT f2.friend FROM Friendship f1 " +
            "JOIN Friendship f2 ON f1.friend = f2.user " +
            "WHERE f1.user = :user " +
            "AND f2.friend != :user " +
            "AND f2.friend NOT IN (SELECT f3.friend FROM Friendship f3 WHERE f3.user = :user)")
    Set<User> findFriendsOfFriends(@Param("user") User user);
}
