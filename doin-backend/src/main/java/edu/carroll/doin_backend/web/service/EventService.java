package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * The {@code EventService} interface provides methods for managing events
 * within the application. It allows clients to perform CRUD operations,
 * retrieve events, and manage user participation in events.
 */
public interface EventService {

    Page<EventDTO> getPublicEvents(Pageable pageable);
    Page<EventDTO> getUserEvents(Integer userId, Integer reqUserId, Pageable pageable);


    Page<EventDTO> getAll(Integer userId, Pageable pageable);

    List<EventDTO> getUpcomingEvents(Integer userId);

    EventDTO getById(Integer eventId, Integer userId);

    /**
     * Adds a new event to the system.
     *
     * @param event the {@link EventDTO} object representing the event to add.
     * @return the {@link EventDTO} object representing the newly added event.
     */
    EventDTO add(EventDTO event);

    /**
     * Allows a user to join an event.
     *
     * @param userId  the unique identifier of the user joining the event.
     * @param eventId the unique identifier of the event to join.
     * @throws IllegalArgumentException if the user or event ID is invalid.
     */
    boolean joinUser(Integer userId, Integer eventId);

    /**
     * Deletes an event from the system by its unique identifier.
     *
     * @param id the unique identifier of the event to delete.
     * @throws IllegalArgumentException if the event ID is invalid or does not exist.
     */
    void delete(Integer id);

    boolean addImage(Integer eventId, Integer userId, MultipartFile file);
}