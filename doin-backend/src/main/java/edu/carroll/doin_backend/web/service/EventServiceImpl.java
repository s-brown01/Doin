package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.User;
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
    public List<EventDTO> getAll() {
        return eventRepository.findAll().stream().map(EventDTO::new).toList();
    }

    @Override
    public EventDTO getById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return new EventDTO(event);
    }

    @Override
    public EventDTO add(EventDTO event) {
        Event newEvent = eventRepository.save(new Event(event));
        return new EventDTO(newEvent);
    }

    @Override
    public void joinUser(Integer eventId, Integer userId) {
        Event existing = eventRepository.getById(eventId);
        User user = new User();
        user.setId(userId);
        existing.addJoiner(user);
        eventRepository.save(existing);
    }

    @Override
    public void update(EventDTO event) {
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
