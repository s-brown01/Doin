package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing events.
 * <p>
 * This controller provides endpoints to perform CRUD operations on events.
 * It utilizes the {@link EventService} to interact with event data and perform necessary operations.
 * </p>
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    /**
     * Constructs an EventController with the specified {@link EventService}.
     *
     * @param eventService the service used to manage events
     */
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Retrieves all events.
     * <p>
     * This endpoint returns a list of all existing events in the system.
     * </p>
     *
     * @return a list of {@link EventDTO} representing all events
     */
    @GetMapping()
    public Page<EventDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        return eventService.getAll(pageable);
    }

    /**
     * Retrieves a specific event by its ID.
     * <p>
     * This endpoint returns the event corresponding to the provided ID.
     * </p>
     *
     * @param id the ID of the event to retrieve
     * @return the {@link EventDTO} representing the requested event
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable Integer id) {
        EventDTO event = eventService.getById(id);
        if(event == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    /**
     * Creates a new event.
     * <p>
     * This endpoint accepts an {@link EventDTO} and creates a new event in the system.
     * </p>
     *
     * @param event the event data to create
     * @return the created {@link EventDTO}
     */
    @PostMapping()
    public EventDTO create(@RequestBody EventDTO event) {
        return eventService.add(event);
    }

    /**
     * Joins a user to an event.
     * <p>
     * This endpoint allows a user to join an event by its ID.
     * </p>
     *
     * @param id the ID of the event to join
     * @param userId the ID of the user joining the event
     */
    @PostMapping("/{id}/join")
    public ResponseEntity<Boolean> join(@PathVariable Integer id, @RequestParam Integer userId) {
        boolean res = eventService.joinUser(id, userId);
        return ResponseEntity.ok(res);
    }

    /**
     * Deletes a specific event by its ID.
     * <p>
     * This endpoint removes the event corresponding to the provided ID from the system.
     * </p>
     *
     * @param id the ID of the event to delete
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        eventService.delete(id);
    }
}