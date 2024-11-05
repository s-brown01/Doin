package edu.carroll.doin_backend.web.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.enums.Visibility;
import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class EventServiceImplTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityQuestionService securityQuestionService;

    private User user;
    private Event testEvent;

    @BeforeEach
    public void setup() {
        // Create test user
        securityQuestionService.addSecurityQuestion("pet");
        securityQuestionService.addSecurityQuestion("city");
        securityQuestionService.addSecurityQuestion("school");
        RegisterDTO data = new RegisterDTO("user1", "password", "pet", "answer");
        userService.createNewUser(data);
        user = new User(userService.findUser(null, "user1"));

        // Create base test event
        testEvent = new Event();
        testEvent.setCreator(user);
        testEvent.setVisibility(Visibility.PUBLIC);
        testEvent.setDescription("Test climbing event");
        testEvent.setLocation("Carroll");
        testEvent.setTime(LocalDateTime.now().plusDays(5));
    }

    @Test
    @DisplayName("Should successfully add new event")
    void testAddEvent() {
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));

        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());
        assertEquals(testEvent.getDescription(), savedEvent.getDescription());
        assertEquals(testEvent.getLocation(), savedEvent.getLocation());
        assertEquals(testEvent.getVisibility(), savedEvent.getVisibility());
    }

    @Test
    @DisplayName("Should retrieve all events with pagination")
    void testGetAllEvents() {
        // Add multiple events
        for (int i = 0; i < 15; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(Visibility.PUBLIC);
            event.setDescription("Event " + i);
            event.setTime(LocalDateTime.now().plusDays(i));
            eventService.add(new EventDTO(event));
        }

        // Test first page
        Page<EventDTO> firstPage = eventService.getAll(PageRequest.of(0, 10, Sort.by("time").descending()));
        assertEquals(10, firstPage.getContent().size());
        assertEquals(15, firstPage.getTotalElements());

        // Test second page
        Page<EventDTO> secondPage = eventService.getAll(PageRequest.of(1, 10, Sort.by("time").descending()));
        assertEquals(5, secondPage.getContent().size());
    }

    @Test
    @DisplayName("Should retrieve event by ID when exists")
    void testGetById_Success() {
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        EventDTO retrievedEvent = eventService.getById(savedEvent.getId());

        assertNotNull(retrievedEvent);
        assertEquals(savedEvent.getId(), retrievedEvent.getId());
        assertEquals(savedEvent.getDescription(), retrievedEvent.getDescription());
    }

    @Test
    @DisplayName("Should return null when getting non-existent event")
    void testGetById_EventNotFound() {
        assertNull(eventService.getById(99999));
    }

    @Test
    @DisplayName("Should successfully delete event")
    void testDeleteEvent() {
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(eventService.getById(savedEvent.getId()));

        eventService.delete(savedEvent.getId());
        assertNull(eventService.getById(savedEvent.getId()));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent event")
    void testDeleteNonExistentEvent() {
        assertThrows(ResourceNotFoundException.class, () -> eventService.delete(99999));
    }

    @Test
    @DisplayName("Should successfully join user to event")
    void testJoinUser_Success() {
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));

        // Create second user
        RegisterDTO userData = new RegisterDTO("user2", "password", "pet", "answer");
        userService.createNewUser(userData);
        User secondUser = new User(userService.findUser(null, "user2"));

        assertTrue(eventService.joinUser(savedEvent.getId(), secondUser.getId()));

        // Verify user was added
        EventDTO updatedEvent = eventService.getById(savedEvent.getId());
        assertTrue(updatedEvent.getJoiners().stream()
                .anyMatch(joiner -> joiner.getId().equals(secondUser.getId())));
    }

    @Test
    @DisplayName("Should prevent duplicate user joins")
    void testJoinUser_PreventDuplicates() {
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));

        // Try to join with same user twice
        assertTrue(eventService.joinUser(savedEvent.getId(), user.getId()));
        assertFalse(eventService.joinUser(savedEvent.getId(), user.getId()));
    }

    @Test
    @DisplayName("Should handle sorting in getAllEvents")
    void testGetAllEvents_Sorting() {
        // Add events with different dates
        Event event1 = new Event();
        event1.setCreator(user);
        event1.setVisibility(Visibility.PUBLIC);
        event1.setTime(LocalDateTime.now().plusDays(1));

        Event event2 = new Event();
        event2.setCreator(user);
        event2.setVisibility(Visibility.PUBLIC);
        event2.setTime(LocalDateTime.now().plusDays(2));

        Event event3 = new Event();
        event3.setCreator(user);
        event3.setVisibility(Visibility.PUBLIC);
        event3.setTime(LocalDateTime.now().plusDays(3));

        eventService.add(new EventDTO(event1));
        eventService.add(new EventDTO(event2));
        eventService.add(new EventDTO(event3));

        // Test ascending sort
        Page<EventDTO> eventsAsc = eventService.getAll(PageRequest.of(0, 10, Sort.by("time").ascending()));
        assertTrue(eventsAsc.getContent().get(0).getTime().isBefore(
                eventsAsc.getContent().get(1).getTime()));

        // Test descending sort
        Page<EventDTO> eventsDesc = eventService.getAll(PageRequest.of(0, 10, Sort.by("time").descending()));
        assertTrue(eventsDesc.getContent().get(0).getTime().isAfter(
                eventsDesc.getContent().get(1).getTime()));
    }
    @Test
    @DisplayName("Creator should not be able to join their own event")
    void testCreatorCannotJoinOwnEvent() {
        // Create and save an event
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));

        // Attempt to join as creator
        boolean joinResult = eventService.joinUser(savedEvent.getId(), user.getId());

        // Verify join was unsuccessful
        assertFalse(joinResult, "Creator should not be able to join their own event");

        // Verify creator is not in joiners list
        EventDTO event = eventService.getById(savedEvent.getId());
        assertTrue(event.getJoiners().isEmpty(), "Joiners list should be empty");
    }

    @Test
    @DisplayName("Users should not be able to join past events")
    void testCannotJoinPastEvent() {
        // Create an event in the past
        testEvent.setTime(LocalDateTime.now().minusDays(1));
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));

        // Create a second user
        RegisterDTO userData = new RegisterDTO("user2", "password", "pet", "answer");
        userService.createNewUser(userData);
        User secondUser = new User(userService.findUser(null, "user2"));

        // Attempt to join past event
        boolean joinResult = eventService.joinUser(savedEvent.getId(), secondUser.getId());

        // Verify join was unsuccessful
        assertFalse(joinResult, "Users should not be able to join past events");

        // Verify joiners list is empty
        EventDTO event = eventService.getById(savedEvent.getId());
        assertTrue(event.getJoiners().isEmpty(), "Joiners list should be empty for past event");
    }

    @Test
    @DisplayName("Event time should not be settable to past")
    void testCannotCreateEventInPast() {
        // Create event with past time
        testEvent.setTime(LocalDateTime.now().minusDays(1));

        // Using assertThrows to verify that adding an event with past time throws an exception
        assertThrows(IllegalArgumentException.class, () -> {
            eventService.add(new EventDTO(testEvent));
        }, "Should not be able to create event with past time");
    }

    @Test
    @DisplayName("Multiple users should be able to join future event")
    void testMultipleUsersCanJoinFutureEvent() {
        // Create a future event
        testEvent.setTime(LocalDateTime.now().plusDays(7));
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));

        // Create multiple users
        User[] users = new User[3];
        for (int i = 0; i < 3; i++) {
            RegisterDTO userData = new RegisterDTO("user" + (i + 2), "password", "pet", "answer");
            userService.createNewUser(userData);
            users[i] = new User(userService.findUser(null, "user" + (i + 2)));
        }

        // Join all users to event
        for (User user : users) {
            assertTrue(eventService.joinUser(savedEvent.getId(), user.getId()),
                    "Each user should be able to join future event");
        }

        // Verify all users are in joiners list
        EventDTO event = eventService.getById(savedEvent.getId());
        assertEquals(3, event.getJoiners().size(), "All users should be in joiners list");

        // Verify correct users are in joiners list
        for (User user : users) {
            assertTrue(event.getJoiners().stream()
                            .anyMatch(joiner -> joiner.getId().equals(user.getId())),
                    "Each user should be present in joiners list");
        }
    }
}