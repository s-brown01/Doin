package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.enums.Visibility;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.EventService;
import edu.carroll.doin_backend.web.service.SecurityQuestionService;
import edu.carroll.doin_backend.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EventControllerTest {
    private final String username1 = "event_user_1";
    private final String username2 = "event_user_2";
    private String user1Header;
    private String user2Header;
    private String invalidAuthHeader;
    private EventDTO testEvent;
    private UserDTO user1;
    private UserDTO user2;


    @Autowired
    private EventController eventController;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityQuestionService sqService;

    @BeforeEach
    public void setUp() {
        // Add security question
        sqService.addSecurityQuestion("test question");

        // Create test users
        createNewUser(username1);
        createNewUser(username2);

        // Generate auth tokens
        final String user1Token = tokenService.generateToken(username1, userService.findUser(null, username1).getId());
        final String user2Token = tokenService.generateToken(username2, userService.findUser(null, username2).getId());

        final String invalidToken = user1Token + "INVALID";
        user1Header = "Bearer " + user1Token;
        user2Header = "Bearer " + user2Token;

        invalidAuthHeader = "Bearer " + invalidToken;

        // Set up user1 data
        user1 = new UserDTO();
        user1.setId(userService.findUser(null, username1).getId());
        user1.setUsername(username1);

        user2 = new UserDTO();
        user2.setId(userService.findUser(null, username2).getId());
        user2.setUsername(username2);

        // Create test event
        testEvent = new EventDTO();
        testEvent.setDescription("Test Event");
        testEvent.setDescription("Test Description");
        testEvent.setLocation("Test Location");
        testEvent.setTime(LocalDateTime.now().plusDays(1));
        testEvent.setVisibility(Visibility.PUBLIC);
        testEvent.setCreator(user1);
    }

    private void createNewUser(String username) {
        RegisterDTO data = new RegisterDTO(username, "password", "test question", "answer");
        userService.createNewUser(data);
    }

    @Test
    public void createEvent_Success() {
        // Create new event
        EventDTO createdEvent = eventController.create(testEvent, user1Header);

        // Verify event was created successfully
        assertNotNull(createdEvent, "Created event should not be null");
        assertEquals(testEvent.getDescription(), createdEvent.getDescription(), "Event title should match");
        assertEquals(testEvent.getDescription(), createdEvent.getDescription(), "Event description should match");
        assertEquals(testEvent.getLocation(), createdEvent.getLocation(), "Event location should match");
        assertEquals(user1.getId(), createdEvent.getCreator().getId(), "Event creator should match");
    }

    @Test
    public void getById_Success() {
        // First create an event
        EventDTO createdEvent = eventController.create(testEvent, user1Header);

        // Get event by ID
        ResponseEntity<EventDTO> response = eventController.getById(createdEvent.getId(), user1Header);

        // Verify successful retrieval
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return OK status");
        assertNotNull(response.getBody(), "Retrieved event should not be null");
        assertEquals(createdEvent.getId(), response.getBody().getId(), "Event IDs should match");
        assertEquals(createdEvent.getDescription(), response.getBody().getDescription(), "Event titles should match");
    }

    @Test
    public void getById_NotFound() {
        // Attempt to get non-existent event
        ResponseEntity<EventDTO> response = eventController.getById(999999, user1Header);

        // Verify not found response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return NOT_FOUND status for non-existent event");
    }

    @Test
    public void getUpcoming_Success() {
        // Create test event
        eventController.create(testEvent, user1Header);

        // Get upcoming events
        List<EventDTO> upcomingEvents = eventController.getUpcoming(user1Header);

        // Verify retrieval
        assertNotNull(upcomingEvents, "Upcoming events list should not be null");
        assertFalse(upcomingEvents.isEmpty(), "Should have at least one upcoming event");
        assertTrue(upcomingEvents.stream()
                        .anyMatch(event -> event.getDescription().equals(testEvent.getDescription())),
                "Should contain the created test event");
    }

    @Test
    public void getUpcoming_Unauthorized() {
        // Attempt to get upcoming events with invalid auth
        List<EventDTO> upcomingEvents = eventController.getUpcoming(invalidAuthHeader);

        // Verify failure
        assertTrue(upcomingEvents == null || upcomingEvents.isEmpty(),
                "Should return null or empty list for unauthorized request");
    }

    @Test
    public void joinEvent_Success() {
        // Create event
        testEvent.setTime(LocalDateTime.now());
        EventDTO createdEvent = eventController.create(testEvent, user1Header);

        // Join event
        ResponseEntity<Boolean> response = eventController.join(createdEvent.getId(), user2Header);

        // Verify join success
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return OK status");
        assertTrue(response.getBody(), "Join operation should be successful");
    }

    @Test
    public void joinEvent_InvalidEvent() {
        // Attempt to join non-existent event
        ResponseEntity<Boolean> response = eventController.join(999999, user1Header);

        // Verify failure
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return OK status");
        assertFalse(response.getBody(), "Join operation should fail for invalid event");
    }

    // Test for `getAll`
    @Test
    public void getAll_Success() {
        Page<EventDTO> eventsPage = eventController.getAll(0, 10, user1Header);

        assertNotNull(eventsPage, "Event page should not be null");
        assertTrue(eventsPage.getTotalElements() >= 0, "Total elements should be >= 0");
    }

    @Test
    public void getAll_Unauthorized() {
        Page<EventDTO> eventsPage = eventController.getAll(0, 10, invalidAuthHeader);

        assertNotNull(eventsPage, "Unauthorized request should still return a page object");
        assertEquals(0, eventsPage.getTotalElements(), "Total elements should be 0 for unauthorized access");
    }

    // Test for `getPublicEvents`
    @Test
    public void getPublicEvents_Success() {
        Page<EventDTO> publicEvents = eventController.getPublicEvents(0, 10);

        assertNotNull(publicEvents, "Public events page should not be null");
        assertTrue(publicEvents.getTotalElements() >= 0, "Total elements should be >= 0");
    }

    // Test for `getUserEvents`
    @Test
    public void getUserEvents_Success() {
        Page<EventDTO> userEvents = eventController.getUserEvents(user1.getId(), 0, 10, user1Header);

        assertNotNull(userEvents, "User events page should not be null");
        assertTrue(userEvents.getTotalElements() >= 0, "Total elements should be >= 0");
    }

    @Test
    public void getUserEvents_Unauthorized() {
        Page<EventDTO> userEvents = eventController.getUserEvents(user1.getId(), 0, 10, invalidAuthHeader);

        assertNotNull(userEvents, "Unauthorized request should still return a page object");
        assertEquals(0, userEvents.getTotalElements(), "Total elements should be 0 for unauthorized access");
    }

    // Test for `addImages`
    @Test
    public void addImages_Success() {
        testEvent.setTime(LocalDateTime.now());
        EventDTO createdEvent = eventController.create(testEvent, user1Header);
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test data".getBytes());

        boolean result = eventController.addImages(file, user1Header, createdEvent.getId());

        assertTrue(result, "Image should be added successfully");
    }

    @Test
    public void addImages_Failure() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test data".getBytes());

        boolean result = eventController.addImages(file, invalidAuthHeader, 999999);

        assertFalse(result, "Image addition should fail for invalid event or auth header");
    }
}