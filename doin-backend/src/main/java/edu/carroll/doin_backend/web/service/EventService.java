package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<EventDTO> getAll();
    EventDTO getById(Integer id);
    EventDTO add(EventDTO event);
    void update(EventDTO event);
    void delete(Integer id);

}
