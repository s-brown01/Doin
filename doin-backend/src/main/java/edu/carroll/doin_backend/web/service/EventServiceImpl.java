package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Page<EventDTO> getAll(Pageable pageable) {
        logger.info("Retrieving events with paging, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Event> eventPage = eventRepository.findAll(pageable);
        Page<EventDTO> eventDTOPage = eventPage.map(EventDTO::new);

        logger.info("Successfully retrieved {} events", eventDTOPage.getTotalElements());
        return eventDTOPage;
    }

    @Override
    public EventDTO getById(Integer id) {
        logger.info("Retrieving event by ID: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Event with ID {} not found", id);
                    return new ResourceNotFoundException("Event not found");
                });
        logger.info("Successfully retrieved event with ID: {}", id);
        return new EventDTO(event);
    }

    @Override
    public EventDTO add(EventDTO event) {
        logger.info("Adding new event: {}", event);
        Event newEvent = eventRepository.save(new Event(event));
        logger.info("Successfully added event with ID: {}", newEvent.getId());
        return new EventDTO(newEvent);
    }

    @Override
    public boolean joinUser(Integer eventId, Integer userId) {
        logger.info("User with ID {} joining event with ID {}", userId, eventId);
        Event existing = eventRepository.getById(eventId);
        if(existing.getJoiners().stream().anyMatch(a-> Objects.equals(a.getId(), userId))) {
            return false;
        }
        User user = new User();
        user.setId(userId);
        existing.addJoiner(user);
        eventRepository.save(existing);
        logger.info("User with ID {} successfully joined event with ID {}", userId, eventId);
        return true;
    }

    @Override
    public void update(EventDTO event) {
        logger.info("Updating event with ID {}", event.getId());
        Event existing = eventRepository.getById(event.getId());

        existing.setDescription(event.getDescription());
        existing.setImages(event.getImages());

        eventRepository.save(existing);
        logger.info("Successfully updated event with ID {}", event.getId());
    }

    @Override
    public void delete(Integer id) {
        logger.info("Deleting event with ID {}", id);
        if (!eventRepository.existsById(id)) {
            logger.error("Event with ID {} not found", id);
            throw new ResourceNotFoundException("Event not found");
        }
        eventRepository.deleteById(id);
        logger.info("Successfully deleted event with ID {}", id);
    }
}