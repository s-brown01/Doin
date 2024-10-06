package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

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
}