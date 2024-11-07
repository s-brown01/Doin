package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Repository interface for managing {@link Event} entities.
 * <p>
 * This interface extends {@link JpaRepository}, providing basic CRUD operations and the ability to
 * query the database for Event entities. The primary key for Event entities is of type {@code Integer}.
 * </p>
 *
 * @see JpaRepository
 * @see Event
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e " +
            "FROM Event e " +
            "LEFT JOIN e.joiners j " +
            "WHERE (j.id = :userId or e.creator.id = :userId) AND e.time > CURRENT_TIMESTAMP " +
            "ORDER BY e.time ASC")
    List<Event> getUpcomingEvents(@Param("userId") Integer userId);

    @Query("SELECT e FROM Event e " +
            "WHERE e.visibility = 'PUBLIC' " +
            "OR e.creator.id IN :friendIds")
    Page<Event> findPublicOrFriendsEvents(@Param("friendIds") Set<Integer> friendIds, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.visibility = 'PUBLIC'")
    Page<Event> findAllPublicEvents(Pageable pageable);
}