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
    Set<Friendship> findByFriendAndStatus(User user, FriendshipStatus status);
    Set<Friendship> findByUserAndStatus(User user, FriendshipStatus status);

    Set<Friendship> findByUser(User user);

    boolean existsFriendshipByUserAndFriend(User user, User friend);

    Friendship findByUserAndFriend(User user, User friend);

}
