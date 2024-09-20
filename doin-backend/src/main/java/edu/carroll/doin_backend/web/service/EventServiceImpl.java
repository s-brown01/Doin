package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event getById(Integer id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @Override
    public Event add(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void update(Event event) {
        Event existing = eventRepository.getById(event.getId());

        existing.setDescription(event.getDescription());
        existing.setImages(event.getImages());

        eventRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        eventRepository.deleteById(id);
    }
}
