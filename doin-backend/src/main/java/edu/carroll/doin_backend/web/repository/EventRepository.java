package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
