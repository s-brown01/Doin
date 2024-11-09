package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.enums.Visibility;
import edu.carroll.doin_backend.web.model.Event;
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
 * This interface extends {@link JpaRepository}, providing basic CRUD operations and custom queries
 * for retrieving events based on specific criteria.
 * </p>
 *
 * @see JpaRepository
 * @see Event
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * Retrieves a list of upcoming events that the specified user has joined or created.
     * Only future events (based on current timestamp) are returned.
     *
     * @param userId the ID of the user
     * @return a list of {@link Event} objects representing upcoming events
     */
    @Query("SELECT e " +
            "FROM Event e " +
            "LEFT JOIN e.joiners j " +
            "WHERE (j.id = :userId OR e.creator.id = :userId) AND e.time > CURRENT_TIMESTAMP " +
            "ORDER BY e.time ASC")
    List<Event> getUpcomingEvents(@Param("userId") Integer userId);

    /**
     * Retrieves a paginated list of public events or events created by the user's friends.
     *
     * @param friendIds the set of friend IDs whose events to include
     * @param pageable  the pagination information
     * @return a {@link Page} of {@link Event} objects representing public or friends' events
     */
    @Query("SELECT e FROM Event e " +
            "WHERE e.visibility = 'PUBLIC' " +
            "OR e.creator.id IN :friendIds")
    Page<Event> findPublicOrFriendsEvents(@Param("friendIds") Set<Integer> friendIds, Pageable pageable);

    /**
     * Retrieves a paginated list of events created by a specific user based on visibility.
     * If the visibility is 'PRIVATE', all private events of the user are returned.
     *
     * @param userId     the ID of the user whose events to retrieve
     * @param visibility the visibility filter for events
     * @param pageable   the pagination information
     * @return a {@link Page} of {@link Event} objects representing the user's events
     */
    @Query("SELECT e FROM Event e " +
            "WHERE e.creator.id = :userId " +
            "AND (e.visibility = :visibility OR :visibility = 'PRIVATE')")
    Page<Event> findUserEvents(@Param("userId") Integer userId,
                               @Param("visibility") Visibility visibility,
                               Pageable pageable);

    /**
     * Retrieves a paginated list of all public events.
     *
     * @param pageable the pagination information
     * @return a {@link Page} of {@link Event} objects representing public events
     */
    @Query("SELECT e FROM Event e WHERE e.visibility = 'PUBLIC'")
    Page<Event> findAllPublicEvents(Pageable pageable);

    /**
     * Retrieves a paginated list of events created by a specific user.
     *
     * @param userId   the ID of the user whose public events to retrieve
     * @param pageable the pagination information
     * @return a {@link Page} of {@link Event} objects representing the user's events
     */
    @Query("SELECT e FROM Event e WHERE e.creator.id = :userId")
    Page<Event> findAllPublicEvents(@Param("userId") Integer userId, Pageable pageable);
}