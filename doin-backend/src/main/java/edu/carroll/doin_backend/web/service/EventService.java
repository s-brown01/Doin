package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAll();
    Event getById(Integer id);
    Event add(Event event);
    void update(Event event);
    void delete(Integer id);

}
