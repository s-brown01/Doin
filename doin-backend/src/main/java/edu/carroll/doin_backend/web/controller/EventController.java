package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private final TokenService tokenService;
    private final EventService eventService;

    /**
     * Constructs an EventController with the specified {@link EventService} and {@link TokenService}.
     *
     * @param eventService the service used to manage events
     * @param tokenService the service used to manage token-based authentication
     */
    public EventController(EventService eventService, TokenService tokenService) {
        this.eventService = eventService;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves all events with pagination.
     *
     * @param page       the page number to retrieve
     * @param size       the size of the page
     * @param authHeader the authorization header containing the user's token
     * @return a page of {@link EventDTO} representing the events
     */
    @GetMapping()
    public Page<EventDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Fetching all events - Page: {}, Size: {}", page, size);
        Integer userId = tokenService.getUserId(authHeader);
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        Page<EventDTO> events = eventService.getAll(userId, pageable);
        logger.debug("Retrieved {} events for userId {}", events.getTotalElements(), userId);
        return events;
    }

    /**
     * Retrieves public events with pagination.
     *
     * @param page the page number to retrieve
     * @param size the size of the page
     * @return a page of {@link EventDTO} representing public events
     */
    @GetMapping("/public")
    public Page<EventDTO> getPublicEvents(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching public events - Page: {}, Size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        Page<EventDTO> publicEvents = eventService.getPublicEvents(pageable);
        logger.debug("Retrieved {} public events", publicEvents.getTotalElements());
        return publicEvents;
    }

    /**
     * Retrieves events created by a specific user.
     *
     * @param id         the ID of the user whose events to retrieve
     * @param page       the page number to retrieve
     * @param size       the size of the page
     * @param authHeader the authorization header containing the user's token
     * @return a page of {@link EventDTO} representing the user's events
     */
    @GetMapping("/users/{id}")
    public Page<EventDTO> getUserEvents(@PathVariable Integer id,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Fetching events for user ID: {} - Page: {}, Size: {}", id, page, size);
        Integer userId = tokenService.getUserId(authHeader);
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        Page<EventDTO> userEvents = eventService.getUserEvents(id, userId, pageable);
        logger.debug("Retrieved {} events for userId {}", userEvents.getTotalElements(), id);
        return userEvents;
    }

    /**
     * Retrieves upcoming events for the authenticated user.
     *
     * @param authHeader the authorization header containing the user's token
     * @return a list of {@link EventDTO} representing upcoming events
     */
    @GetMapping("/upcoming")
    public List<EventDTO> getUpcoming(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Fetching upcoming events for authenticated user");
        Integer userId = tokenService.getUserId(authHeader);
        List<EventDTO> upcomingEvents = eventService.getUpcomingEvents(userId);
        logger.debug("Retrieved {} upcoming events for userId {}", upcomingEvents.size(), userId);
        return upcomingEvents;
    }

    /**
     * Retrieves a specific event by its ID.
     *
     * @param id         the ID of the event to retrieve
     * @param authHeader the authorization header containing the user's token
     * @return a {@link ResponseEntity} with the event data or not found status
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable Integer id,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Fetching event with ID: {}", id);
        Integer userId = tokenService.getUserId(authHeader);
        EventDTO event = eventService.getById(id, userId);
        if (event == null) {
            logger.warn("Event with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Retrieved event with ID {}", id);
        return ResponseEntity.ok(event);
    }

    /**
     * Creates a new event.
     *
     * @param event      the event data to create
     * @param authHeader the authorization header containing the user's token
     * @return the created {@link EventDTO}
     */
    @PostMapping()
    public EventDTO create(@RequestBody EventDTO event, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Integer userId = tokenService.getUserId(authHeader);
        logger.info("Creating a new event by userId {}", userId);
        if (!event.getCreator().getId().equals(userId)) {
            logger.warn("Unauthorized event creation attempt by userId {}", userId);
            return null;
        }
        EventDTO createdEvent = eventService.add(event);
        logger.debug("Created event with ID {}", createdEvent.getId());
        return createdEvent;
    }

    /**
     * Adds the authenticated user to the event participants list.
     *
     * @param id         the ID of the event to join
     * @param authHeader the authorization header containing the user's token
     * @return a {@link ResponseEntity} with the result of the join operation
     */
    @PostMapping("/{id}/join")
    public ResponseEntity<Boolean> join(@PathVariable Integer id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Integer userId = tokenService.getUserId(authHeader);
        logger.info("User ID {} attempting to join event ID {}", userId, id);
        boolean result = eventService.joinUser(id, userId);
        logger.debug("User ID {} joined event ID {}: {}", userId, id, result);
        return ResponseEntity.ok(result);
    }

    /**
     * Adds an image to the specified event.
     *
     * @param file       the image file to add
     * @param authHeader the authorization header containing the user's token
     * @param id         the ID of the event
     * @return true if the image was added successfully, false otherwise
     */
    @PostMapping("{id}/images")
    public boolean addImages(@RequestParam("file") MultipartFile file,
                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                             @PathVariable Integer id) {
        Integer userId = tokenService.getUserId(authHeader);
        logger.info("Adding image to event ID {} by user ID {}", id, userId);
        boolean result = eventService.addImage(id, userId, file);
        logger.debug("Image added to event ID {}: {}", id, result);
        return result;
    }
}