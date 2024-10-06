package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * The {@code EventServiceImpl} class implements the {@link EventService} interface,
 * providing concrete methods for managing events in the application.
 * This class uses an {@link EventRepository} to perform CRUD operations on events.
 */
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    /**
     * Constructs a new {@code EventServiceImpl} with the specified {@link EventRepository}.
     *
     * @param eventRepository the repository used to access event data.
     */
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieves all events from the repository, sorted by time in descending order.
     *
     * @return a list of {@link EventDTO} representing all events.
     */
    @Override
    public List<EventDTO> getAll() {
        return eventRepository.findAll().stream()
                .sorted(Comparator.comparing(Event::getTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(EventDTO::new)
                .toList();
    }

    /**
     * Retrieves an event by its unique identifier.
     *
     * @param id the unique identifier of the event to retrieve.
     * @return an {@link EventDTO} representing the event.
     * @throws ResourceNotFoundException if no event with the specified ID exists.
     */
    @Override
    public EventDTO getById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return new EventDTO(event);
    }

    /**
     * Adds a new event to the repository.
     *
     * @param event the {@link EventDTO} object representing the event to add.
     * @return the {@link EventDTO} object representing the newly added event.
     */
    @Override
    public EventDTO add(EventDTO event) {
        Event newEvent = eventRepository.save(new Event(event));
        return new EventDTO(newEvent);
    }

    /**
     * Allows a user to join an event by adding the user to the event's joiners.
     *
     * @param eventId the unique identifier of the event.
     * @param userId  the unique identifier of the user joining the event.
     * @throws ResourceNotFoundException if no event with the specified ID exists.
     */
    @Override
    public void joinUser(Integer eventId, Integer userId) {
        Event existing = eventRepository.getById(eventId);
        User user = new User();
        user.setId(userId);
        existing.addJoiner(user);
        eventRepository.save(existing);
    }

    /**
     * Updates an existing event in the repository.
     *
     * @param event the {@link EventDTO} object containing the updated event information.
     * @throws ResourceNotFoundException if no event with the specified ID exists.
     */
    @Override
    public void update(EventDTO event) {
        Event existing = eventRepository.getById(event.getId());

        existing.setDescription(event.getDescription());
        existing.setImages(event.getImages());

        eventRepository.save(existing);
    }

    /**
     * Deletes an event from the repository by its unique identifier.
     *
     * @param id the unique identifier of the event to delete.
     * @throws ResourceNotFoundException if no event with the specified ID exists.
     */
    @Override
    public void delete(Integer id) {
        eventRepository.deleteById(id);
    }
}