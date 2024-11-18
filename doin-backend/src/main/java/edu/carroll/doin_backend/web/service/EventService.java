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

    /**
     * Retrieves a paginated list of public events.
     *
     * @param pageable the pagination information.
     * @return a {@link Page} of {@link EventDTO} objects representing public events.
     */
    Page<EventDTO> getPublicEvents(Pageable pageable);

    /**
     * Retrieves a paginated list of events created by a specific user, including events visible to the requester.
     *
     * @param userId    the unique identifier of the user whose events are being retrieved.
     * @param reqUserId the unique identifier of the requesting user.
     * @param pageable  the pagination information.
     * @return a {@link Page} of {@link EventDTO} objects representing the user's events.
     */
    Page<EventDTO> getUserEvents(Integer userId, Integer reqUserId, Pageable pageable);

    /**
     * Retrieves a paginated list of all events visible to the given user.
     *
     * @param userId   the unique identifier of the requesting user.
     * @param pageable the pagination information.
     * @return a {@link Page} of {@link EventDTO} objects representing all events visible to the user.
     */
    Page<EventDTO> getAll(Integer userId, Pageable pageable);

    /**
     * Retrieves a list of upcoming events for a specific user.
     *
     * @param userId the unique identifier of the user.
     * @return a list of {@link EventDTO} objects representing upcoming events.
     */
    List<EventDTO> getUpcomingEvents(Integer userId);

    /**
     * Retrieves an event by its unique identifier and user context.
     *
     * @param eventId the unique identifier of the event.
     * @param userId  the unique identifier of the requesting user.
     * @return the {@link EventDTO} object representing the retrieved event.
     * @throws IllegalArgumentException if the event ID or user ID is invalid.
     */
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
     * @return {@code true} if the user successfully joined the event; {@code false} otherwise.
     * @throws IllegalArgumentException if the user or event ID is invalid.
     */
    boolean joinUser(Integer userId, Integer eventId);

    /**
     * Adds an image to an event.
     *
     * @param eventId the unique identifier of the event.
     * @param userId  the unique identifier of the user adding the image.
     * @param file    the {@link MultipartFile} representing the image file.
     * @return {@code true} if the image was successfully added; {@code false} otherwise.
     * @throws IllegalArgumentException if the event ID or user ID is invalid, or if the file is null.
     */
    boolean addImage(Integer eventId, Integer userId, MultipartFile file);
}