package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.EventService;
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
    private final TokenService tokenService;
    private final EventService eventService;

    /**
     * Constructs an EventController with the specified {@link EventService}.
     *
     * @param eventService the service used to manage events
     */
    public EventController(EventService eventService, TokenService tokenService) {
        this.eventService = eventService;
        this.tokenService = tokenService;
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
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Integer userId = tokenService.getUserId(authHeader);
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        return eventService.getAll(userId, pageable);
    }

    @GetMapping("/public")
    public Page<EventDTO> getPublicEvents(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        return eventService.getPublicEvents(pageable);
    }

    @GetMapping("/users/{id}")
    public Page<EventDTO> getUserEvents(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        return eventService.getPublicEvents(pageable);
    }

    @GetMapping("/upcoming")
    public List<EventDTO> getUpcoming(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        Integer userId = tokenService.getUserId(authHeader);
        return eventService.getUpcomingEvents(userId);
    }
//
//    @GetMapping("/user/{id}")
//    public List<EventDTO> getUserEvents(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable String id){
//        Integer currUserId = tokenService.getUserId(authHeader);
//        return eventService.getUpcomingEvents(userId);
//    }

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
    public ResponseEntity<EventDTO> getById(@PathVariable Integer id,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Integer userId = tokenService.getUserId(authHeader);
        EventDTO event = eventService.getById(id, userId);
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
    public EventDTO create(@RequestBody EventDTO event, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Integer userId = tokenService.getUserId(authHeader);
        if(!event.getCreator().getId().equals(userId))
            return null;
        return eventService.add(event);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Boolean> join(@PathVariable Integer id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Integer userId = tokenService.getUserId(authHeader);
        boolean res = eventService.joinUser(id, userId);
        return ResponseEntity.ok(res);
    }

    @PostMapping("{id}/images")
    public boolean addImages(@RequestParam("file") MultipartFile file,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                             @PathVariable Integer id) {
        Integer userId = tokenService.getUserId(authHeader);
        return eventService.addImage(id, userId, file);
    }
}