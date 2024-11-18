package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.enums.Visibility;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityQuestionService securityQuestionService;

    @Autowired
    private FriendService friendService;

    private User user;
    private User user2;
    private Event testEvent;

    @BeforeEach
    public void setup() {
        // Create test user
        assertTrue(securityQuestionService.addSecurityQuestion("pet"));
        assertTrue(securityQuestionService.addSecurityQuestion("city"));
        assertTrue(securityQuestionService.addSecurityQuestion("school"));
        RegisterDTO data = new RegisterDTO("user1", "password", "pet", "answer");
        assertTrue(userService.createNewUser(data));
        user = new User(userService.findUser(null, "user1"));
        data = new RegisterDTO("user2", "password", "pet", "answer");
        assertTrue(userService.createNewUser(data));
        user2 = new User(userService.findUser(null, "user2"));
        // Create base test event
        testEvent = new Event();
        testEvent.setCreator(user);
        testEvent.setVisibility(Visibility.PUBLIC);
        testEvent.setDescription("Test climbing event");
        testEvent.setLocation("Carroll");
        testEvent.setTime(LocalDateTime.now().plusDays(5));
    }

    @ParameterizedTest
    @DisplayName("Should successfully retrieve public events with valid pagination - Happy Path")
    @ValueSource(ints = {0})  // Page numbers to test
    void testGetPublicEvents_HappyPath(int pageNumber) {
        // Create 5 public events
        for (int i = 0; i < 5; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(Visibility.PUBLIC);
            event.setDescription("Public Event " + i);
            event.setLocation("Location " + i);
            event.setTime(LocalDateTime.now().plusDays(i));
            assertNotNull(eventService.add(new EventDTO(event)));
        }

        // Test with valid pagination parameters
        PageRequest pageRequest = PageRequest.of(pageNumber, 3, Sort.by("time").ascending());
        Page<EventDTO> result = eventService.getPublicEvents(pageRequest);

        // Assertions for successful retrieval
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(5, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.getContent().stream()
                .allMatch(event -> event.getVisibility() == Visibility.PUBLIC));
    }

    @ParameterizedTest
    @DisplayName("Should handle pagination with sorting - Happy Path")
    @ValueSource(strings = {"ascending", "descending"})
    void testGetPublicEvents_WithSorting() {
        // Create 3 events with different timestamps
        LocalDateTime baseTime = LocalDateTime.now();

        Event event1 = new Event();
        event1.setCreator(user);
        event1.setVisibility(Visibility.PUBLIC);
        event1.setDescription("Last Event");
        event1.setTime(baseTime.plusDays(2));
        assertNotNull(eventService.add(new EventDTO(event1)));

        Event event2 = new Event();
        event2.setCreator(user);
        event2.setVisibility(Visibility.PUBLIC);
        event2.setDescription("First Event");
        event2.setTime(baseTime);
        assertNotNull(eventService.add(new EventDTO(event2)));

        Event event3 = new Event();
        event3.setCreator(user);
        event3.setVisibility(Visibility.PUBLIC);
        event3.setDescription("Middle Event");
        event3.setTime(baseTime.plusDays(1));
        assertNotNull(eventService.add(new EventDTO(event3)));

        // Test sorting based on the sortOrder
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("time").ascending());
        Page<EventDTO> result = eventService.getPublicEvents(pageRequest);

        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals("First Event" , result.getContent().get(0).getDescription());
    }

    @ParameterizedTest
    @DisplayName("Should return empty page for null pageable - Crappy Path")
    @ValueSource(strings = {"null"})
    void testGetPublicEvents_NullPageable(String pageable) {
        // Create some events to ensure they're not returned
        Event event = new Event();
        event.setCreator(user);
        event.setVisibility(Visibility.PUBLIC);
        event.setDescription("Test Event");
        event.setTime(LocalDateTime.now());
        assertNotNull(eventService.add(new EventDTO(event)));

        // Test with null pageable
        Page<EventDTO> result = pageable.equals("null") ? eventService.getPublicEvents(null) : eventService.getPublicEvents(PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @DisplayName("Should return empty page for zero page size - Crappy Path")
    @ValueSource(ints = {1, 2})
    void testGetPublicEvents_PageSize(int pageSize) {
        // Create some test events
        Event event1 = new Event();
        event1.setCreator(user);
        event1.setVisibility(Visibility.PUBLIC);
        event1.setDescription("Test Event 1");
        event1.setTime(LocalDateTime.now());
        assertNotNull(eventService.add(new EventDTO(event1)));

        Event event2 = new Event();
        event2.setCreator(user);
        event2.setVisibility(Visibility.PUBLIC);
        event2.setDescription("Test Event 2");
        event2.setTime(LocalDateTime.now());
        assertNotNull(eventService.add(new EventDTO(event2)));

        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Page<EventDTO> result = eventService.getPublicEvents(pageRequest);

        assertNotNull(result);
        assertEquals(pageSize, result.getContent().size());
    }

    @ParameterizedTest
    @DisplayName("Should handle empty result set - Edge Case")
    @ValueSource(ints = {1, 10})
    void testGetPublicEvents_NoEvents(int pageSize) {
        // Test with valid pagination but no events in database
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Page<EventDTO> result = eventService.getPublicEvents(pageRequest);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    @ParameterizedTest
    @DisplayName("Should successfully retrieve user events - Happy Path")
    @ValueSource(ints = {0, 1})  // Page numbers to test
    void testGetUserEvents_HappyPath(int pageNumber) {
        // Create 5 public events and 5 private events
        int events = 10;
        int pageSize = 3;
        for (int i = 0; i < events; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(i % 2 == 0 ? Visibility.PUBLIC : Visibility.PRIVATE); // alternating public/private events
            event.setDescription("Event " + i);
            event.setLocation("Location " + i);
            event.setTime(LocalDateTime.now().plusDays(i));
            assertNotNull(eventService.add(new EventDTO(event)));
        }

        // Test with valid pagination parameters and the same user
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("time").ascending());
        Page<EventDTO> result = eventService.getUserEvents(user.getId(), user.getId(), pageRequest);

        assertNotNull(result);
        assertEquals(pageSize, result.getContent().size());
        assertEquals(events, result.getTotalElements());
        assertEquals(4, result.getTotalPages());
        assertTrue(result.getContent().stream()
                .allMatch(event -> event.getVisibility() == Visibility.PUBLIC || event.getVisibility() == Visibility.PRIVATE));
    }

    @ParameterizedTest
    @DisplayName("Should retrieve events for a user with a friend - Happy Path")
    @ValueSource(ints = {0, 1, 2})
    void testGetUserEvents_Friend_HappyPath(int pageNumber) {
        // Create events for a friend
        int events = 10;
        int pageSize = 3;
        for (int i = 0; i < events; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(Visibility.PRIVATE); // private events
            event.setDescription("Friend Event " + i);
            event.setLocation("Friend Location " + i);
            event.setTime(LocalDateTime.now().plusDays(i));
            assertNotNull(eventService.add(new EventDTO(event)));
        }
        assertNotNull(friendService.addFriend(user.getUsername(), user2.getUsername()));
        assertNotNull(friendService.addFriend(user2.getUsername(), user.getUsername()));

        Integer reqUserId = user2.getId(); // assuming reqUserId is the user requesting the events
        Integer userId = user.getId(); // friend userId

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("time").ascending());
        Page<EventDTO> result = eventService.getUserEvents(userId, reqUserId, pageRequest);

        assertNotNull(result);
        assertEquals(pageSize, result.getContent().size());
        assertEquals(events, result.getTotalElements());
        assertEquals(4, result.getTotalPages());
        assertTrue(result.getContent().stream()
                .allMatch(event -> event.getVisibility() == Visibility.PRIVATE));
    }
    @Test
    @DisplayName("Should handle pagination with sorting - Happy Path")
    void testGetUserEvents_WithSorting() {
        // Create 3 events with different timestamps
        LocalDateTime baseTime = LocalDateTime.now();

        Event event1 = new Event();
        event1.setCreator(user);
        event1.setVisibility(Visibility.PUBLIC);
        event1.setDescription("Last Event");
        event1.setTime(baseTime.plusDays(2));
        assertNotNull(eventService.add(new EventDTO(event1)));

        Event event2 = new Event();
        event2.setCreator(user);
        event2.setVisibility(Visibility.PUBLIC);
        event2.setDescription("First Event");
        event2.setTime(baseTime);
        assertNotNull(eventService.add(new EventDTO(event2)));

        Event event3 = new Event();
        event3.setCreator(user);
        event3.setVisibility(Visibility.PUBLIC);
        event3.setDescription("Middle Event");
        event3.setTime(baseTime.plusDays(1));
        assertNotNull(eventService.add(new EventDTO(event3)));

        // Test sorting ascending
        PageRequest pageRequestAsc = PageRequest.of(0, 10, Sort.by("time").ascending());
        Page<EventDTO> resultAsc = eventService.getUserEvents(user.getId(), user.getId(), pageRequestAsc);

        assertNotNull(resultAsc);
        assertEquals(3, resultAsc.getContent().size());
        assertEquals("First Event", resultAsc.getContent().get(0).getDescription());
        assertEquals("Middle Event", resultAsc.getContent().get(1).getDescription());
        assertEquals("Last Event", resultAsc.getContent().get(2).getDescription());

        // Test sorting descending
        PageRequest pageRequestDesc = PageRequest.of(0, 10, Sort.by("time").descending());
        Page<EventDTO> resultDesc = eventService.getUserEvents(user.getId(), user.getId(), pageRequestDesc);

        assertEquals("Last Event", resultDesc.getContent().get(0).getDescription());
        assertEquals("First Event", resultDesc.getContent().get(2).getDescription());
    }

    @Test
    @DisplayName("Should return empty page for null pageable - Crappy Path")
    void testGetUserEvents_NullPageable() {
        // Test with null pageable
        Page<EventDTO> result = eventService.getUserEvents(user.getId(), user.getId(), null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty result set - Edge Case")
    void testGetUserEvents_NoEvents() {
        // Test with valid pagination but no events in database
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<EventDTO> result = eventService.getUserEvents(user.getId(), user.getId(), pageRequest);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    @DisplayName("Should successfully retrieve public and friends' events with valid pagination - Happy Path")
    void testGetAll_HappyPath() {
        // Create public and friends' events
        for (int i = 0; i < 5; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(i % 2 == 0 ? Visibility.PUBLIC : Visibility.PRIVATE); // alternating public/private events
            event.setDescription("Event " + i);
            event.setLocation("Location " + i);
            event.setTime(LocalDateTime.now().plusDays(i));
            assertNotNull(eventService.add(new EventDTO(event)));
        }

        // Test with valid pagination parameters
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("time").ascending());
        Page<EventDTO> result = eventService.getAll(user.getId(), pageRequest);

        // Assertions for successful retrieval
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(5, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.getContent().stream()
                .allMatch(event -> event.getVisibility() == Visibility.PUBLIC || event.getVisibility() == Visibility.PRIVATE));
    }

    @Test
    @DisplayName("Should retrieve events for a user with a confirmed friend - Happy Path")
    void testGetAll_Friend_HappyPath() {
        // Create events for a friend
        for (int i = 0; i < 5; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(Visibility.PRIVATE); // private events
            event.setDescription("Friend Event " + i);
            event.setLocation("Friend Location " + i);
            event.setTime(LocalDateTime.now().plusDays(i));
            assertNotNull(eventService.add(new EventDTO(event)));
        }
        assertNotNull(friendService.addFriend(user.getUsername(), user2.getUsername()));
        assertNotNull(friendService.addFriend(user2.getUsername(), user.getUsername()));

        Integer reqUserId = user2.getId(); // friend requesting events
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("time").ascending());
        Page<EventDTO> result = eventService.getAll(reqUserId, pageRequest);

        // Assertions for successful retrieval
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(5, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .allMatch(event -> event.getVisibility() == Visibility.PRIVATE));
    }

    @Test
    @DisplayName("Should retrieve only public events for a user without friends - Crappy Path")
    void testGetAll_NoFriends_CrappyPath() {
        // Create public events for a user without confirmed friends
        for (int i = 0; i < 3; i++) {
            Event event = new Event();
            event.setCreator(user2); // another user, no friends with current user
            event.setVisibility(Visibility.PUBLIC); // public events
            event.setDescription("Non-Friend Event " + i);
            event.setLocation("Non-Friend Location " + i);
            event.setTime(LocalDateTime.now().plusDays(i));
            assertNotNull(eventService.add(new EventDTO(event)));
        }

        Integer reqUserId = user.getId(); // requesting user who has no friends
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("time").ascending());
        Page<EventDTO> result = eventService.getAll(reqUserId, pageRequest);

        // Assertions for public events only
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertTrue(result.getContent().stream()
                .allMatch(event -> event.getVisibility() == Visibility.PUBLIC));
    }

    @Test
    @DisplayName("Should handle pagination with sorting for public and friends' events - Happy Path")
    void testGetAll_WithSorting() {
        // Create events with different timestamps for sorting
        LocalDateTime baseTime = LocalDateTime.now();

        Event event1 = new Event();
        event1.setCreator(user);
        event1.setVisibility(Visibility.PUBLIC);
        event1.setDescription("Last Event");
        event1.setTime(baseTime.plusDays(2));
        assertNotNull(eventService.add(new EventDTO(event1)));

        Event event2 = new Event();
        event2.setCreator(user);
        event2.setVisibility(Visibility.PUBLIC);
        event2.setDescription("First Event");
        event2.setTime(baseTime);
        assertNotNull(eventService.add(new EventDTO(event2)));

        Event event3 = new Event();
        event3.setCreator(user);
        event3.setVisibility(Visibility.PUBLIC);
        event3.setDescription("Middle Event");
        event3.setTime(baseTime.plusDays(1));
        assertNotNull(eventService.add(new EventDTO(event3)));

        // Test sorting ascending
        PageRequest pageRequestAsc = PageRequest.of(0, 10, Sort.by("time").ascending());
        Page<EventDTO> resultAsc = eventService.getAll(user.getId(), pageRequestAsc);

        assertNotNull(resultAsc);
        assertEquals(3, resultAsc.getContent().size());
        assertEquals("First Event", resultAsc.getContent().get(0).getDescription());
        assertEquals("Middle Event", resultAsc.getContent().get(1).getDescription());
        assertEquals("Last Event", resultAsc.getContent().get(2).getDescription());

        // Test sorting descending
        PageRequest pageRequestDesc = PageRequest.of(0, 10, Sort.by("time").descending());
        Page<EventDTO> resultDesc = eventService.getAll(user.getId(), pageRequestDesc);

        assertEquals("Last Event", resultDesc.getContent().get(0).getDescription());
        assertEquals("First Event", resultDesc.getContent().get(2).getDescription());
    }

    @Test
    @DisplayName("Should return empty page for null pageable - Crappy Path")
    void testGetAll_NullPageable() {
        // Test with null pageable
        Page<EventDTO> result = eventService.getAll(user.getId(), null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty result set for no events - Edge Case")
    void testGetAll_NoEvents() {
        // Test with valid pagination but no events in the database
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<EventDTO> result = eventService.getAll(user.getId(), pageRequest);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    @DisplayName("Should successfully retrieve upcoming events for a user - Happy Path")
    void testGetUpcomingEvents_HappyPath() {
        // Create 3 upcoming events
        LocalDateTime baseTime = LocalDateTime.now().plusDays(1); // events scheduled for tomorrow
        for (int i = 0; i < 3; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(Visibility.PUBLIC); // public events
            event.setDescription("Upcoming Event " + i);
            event.setLocation("Location " + i);
            event.setTime(baseTime.plusDays(i)); // Event time is one day apart
            assertNotNull(eventService.add(new EventDTO(event)));
        }

        // Test retrieving upcoming events for the user
        List<EventDTO> result = eventService.getUpcomingEvents(user.getId());
        UserDTO userDto = new UserDTO(user);
        // Assertions
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(event -> event.getTime().isAfter(LocalDateTime.now())));
        assertTrue(result.stream().allMatch(event -> event.getCreator().equals(userDto)));
    }

    @Test
    @DisplayName("Should return empty list when no upcoming events are found - Edge Case")
    void testGetUpcomingEvents_NoEvents() {
        // Test retrieving upcoming events for the user when there are no events
        List<EventDTO> result = eventService.getUpcomingEvents(user.getId());

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for non-existing user - Edge Case")
    void testGetUpcomingEvents_NonExistingUser() {
        // Test with a userId that doesn't exist
        Integer nonExistingUserId = -1; // assuming -1 is not a valid user ID

        List<EventDTO> result = eventService.getUpcomingEvents(nonExistingUserId);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should only retrieve future upcoming events - Happy Path")
    void testGetUpcomingEvents_FutureEventsOnly() {
        // Create future events (scheduled for tomorrow and in the future)
        LocalDateTime baseTime = LocalDateTime.now().plusDays(1);
        Event event1 = new Event();
        event1.setCreator(user);
        event1.setVisibility(Visibility.PUBLIC);
        event1.setDescription("Future Event 1");
        event1.setLocation("Location 1");
        event1.setTime(baseTime); // Event scheduled for tomorrow
        assertNotNull(eventService.add(new EventDTO(event1)));

        Event event2 = new Event();
        event2.setCreator(user);
        event2.setVisibility(Visibility.PUBLIC);
        event2.setDescription("Future Event 2");
        event2.setLocation("Location 2");
        event2.setTime(baseTime.plusDays(2)); // Event scheduled for the day after tomorrow
        assertNotNull(eventService.add(new EventDTO(event2)));

        Event event3 = new Event();
        event3.setCreator(user);
        event3.setVisibility(Visibility.PUBLIC);
        event3.setDescription("Past Event");
        event3.setLocation("Location 3");
        event3.setTime(baseTime.minusDays(2)); // Past event
        assertNotNull(eventService.add(new EventDTO(event3)));

        // Test retrieving upcoming events (should only include future events)
        List<EventDTO> result = eventService.getUpcomingEvents(user.getId());

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(event -> event.getTime().isAfter(LocalDateTime.now())));
        assertFalse(result.stream().anyMatch(event -> event.getTime().isBefore(LocalDateTime.now())));
    }

    @Test
    @DisplayName("Should correctly filter upcoming events based on time - Happy Path")
    void testGetUpcomingEvents_FilteredByTime() {
        // Create events with different times (past, present, future)
        LocalDateTime baseTime = LocalDateTime.now();
        Event pastEvent = new Event();
        pastEvent.setCreator(user);
        pastEvent.setVisibility(Visibility.PUBLIC);
        pastEvent.setDescription("Past Event");
        pastEvent.setTime(baseTime.minusDays(1)); // Past event
        assertNotNull(eventService.add(new EventDTO(pastEvent)));

        Event presentEvent = new Event();
        presentEvent.setCreator(user);
        presentEvent.setVisibility(Visibility.PUBLIC);
        presentEvent.setDescription("Present Event");
        presentEvent.setTime(baseTime); // Present event
        assertNotNull(eventService.add(new EventDTO(presentEvent)));

        Event futureEvent = new Event();
        futureEvent.setCreator(user);
        futureEvent.setVisibility(Visibility.PUBLIC);
        futureEvent.setDescription("Future Event");
        futureEvent.setTime(baseTime.plusDays(1)); // Future event
        assertNotNull(eventService.add(new EventDTO(futureEvent)));

        // Test retrieving upcoming events (should exclude past and present events)
        List<EventDTO> result = eventService.getUpcomingEvents(user.getId());

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size()); // Only the future event should be present
        assertTrue(result.stream().allMatch(event -> event.getTime().isAfter(LocalDateTime.now())));
        assertEquals("Future Event", result.get(0).getDescription());
    }

    @Test
    @DisplayName("Should correctly map Event to EventDTO")
    void testGetUpcomingEvents_EntityToDTOMapping() {
        // Create a future event
        LocalDateTime baseTime = LocalDateTime.now().plusDays(1);
        Event event = new Event();
        event.setCreator(user);
        event.setVisibility(Visibility.PUBLIC);
        event.setDescription("Mapped Event");
        event.setLocation("Mapped Location");
        event.setTime(baseTime);
        assertNotNull(eventService.add(new EventDTO(event)));

        // Test retrieving upcoming events
        List<EventDTO> result = eventService.getUpcomingEvents(user.getId());

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        EventDTO eventDTO = result.get(0);
        assertEquals(event.getDescription(), eventDTO.getDescription());
        assertEquals(event.getLocation(), eventDTO.getLocation());
        assertEquals(event.getTime(), eventDTO.getTime());
        assertEquals(new UserDTO(event.getCreator()), eventDTO.getCreator());
    }

    @Test
    @DisplayName("Should successfully retrieve a public event by ID")
    void testGetById_PublicEvent() {
        // Create private event
        testEvent.setVisibility(Visibility.PRIVATE);
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(savedEvent);
        // Create second user
        RegisterDTO userData = new RegisterDTO("user2", "password", "pet", "answer");
        userService.createNewUser(userData);
        User secondUser = new User(userService.findUser(null, "user2"));

        // Test creator access
        assertNotNull(eventService.getById(savedEvent.getId(), user.getId()));

        // Test non-friend access (should be null)
        assertNull(eventService.getById(savedEvent.getId(), secondUser.getId()));

        // Add friendship
        assertNotNull(friendService.addFriend(user.getUsername(), secondUser.getUsername()));
        assertNotNull(friendService.addFriend(secondUser.getUsername(), user.getUsername()));

        // Test friend access
        assertNotNull(eventService.getById(savedEvent.getId(), secondUser.getId()));
    }

    @Test
    @DisplayName("Should successfully retrieve an event created by the user")
    void testGetById_EventCreatedByUser() {
        // Arrange
        Event event = new Event();
        event.setVisibility(Visibility.PRIVATE); // Private event
        event.setCreator(user);
        event.setDescription("User's Event");
        EventDTO addedEvent = eventService.add(new EventDTO(event));
        assertNotNull(addedEvent);
        // Act
        EventDTO result = eventService.getById(addedEvent.getId(), user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(addedEvent.getId(), result.getId());
        assertEquals("User's Event", result.getDescription());
    }

    @Test
    @DisplayName("Should return null when event is private and user is not creator or friend")
    void testGetById_PrivateEventNoAccess() {
        // Arrange
        Event event = new Event();
        event.setId(3);
        event.setVisibility(Visibility.PRIVATE); // Private event
        event.setCreator(user);
        event.setDescription("Private Event");
        assertNotNull(eventService.add(new EventDTO(event)));

        // Act
        EventDTO result = eventService.getById(3, 999); // A different user (ID 999)

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should successfully retrieve a private event if user is a confirmed friend of the creator")
    void testGetById_PrivateEventWithFriendAccess() {
        // Arrange
        Event event = new Event();
        event.setId(4);
        event.setVisibility(Visibility.PRIVATE); // Private event
        event.setCreator(user);
        event.setDescription("Private Event with Friend");
        EventDTO addedEvent = eventService.add(new EventDTO(event));
        assertNotNull(addedEvent);
        assertNotNull(friendService.addFriend(user.getUsername(), user2.getUsername()));
        assertNotNull(friendService.addFriend(user2.getUsername(), user.getUsername()));

        // Act
        EventDTO result = eventService.getById(addedEvent.getId(), user2.getId()); // The user is a confirmed friend of the creator

        // Assert
        assertNotNull(result);
        assertEquals(addedEvent.getId(), result.getId());
        assertEquals("Private Event with Friend", result.getDescription());
    }

    @Test
    @DisplayName("Should return null when the event does not exist")
    void testGetById_EventNotFound() {
        // Act
        EventDTO result = eventService.getById(9999, 123); // Non-existing event ID

        // Assert
        assertNull(result);
    }


    @Test
    @DisplayName("Should return null when event is private and user is neither creator nor confirmed friend")
    void testGetById_PrivateEventNoAccessWithRestrictedVisibility() {
        // Arrange
        Event event = new Event();
        event.setId(6);
        event.setVisibility(Visibility.PRIVATE); // Private event
        event.setCreator(user);
        event.setDescription("Private Event - Restricted Access");
        assertNotNull(eventService.add(new EventDTO(event)));

        User anotherUser = new User();
        anotherUser.setId(777); // A different user
        // No confirmed friendship between them

        // Act
        EventDTO result = eventService.getById(6, anotherUser.getId());

        // Assert
        assertNull(result); // Should be null, no access to private event
    }

    @Test
    @DisplayName("Should return null when event is private and no friendship exists")
    void testGetById_PrivateEventNoFriendship() {
        // Arrange
        Event event = new Event();
        event.setId(7);
        event.setVisibility(Visibility.PRIVATE);
        event.setCreator(user);
        event.setDescription("Private Event No Friend Access");
        assertNotNull(eventService.add(new EventDTO(event)));

        User userWithoutFriendship = new User();
        userWithoutFriendship.setId(555); // A user without friendship

        // Act
        EventDTO result = eventService.getById(7, userWithoutFriendship.getId());

        // Assert
        assertNull(result); // Should be null, no access
    }

    //

    @Test
    @DisplayName("Should successfully add new event")
    void testAddEvent_simple() {
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(savedEvent);
        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());
        assertEquals(testEvent.getDescription(), savedEvent.getDescription());
        assertEquals(testEvent.getLocation(), savedEvent.getLocation());
        assertEquals(testEvent.getVisibility(), savedEvent.getVisibility());
    }
    @Test
    @DisplayName("Should handle null values in event creation")
    void testAddEvent_NullValues() {
        // Event with null description
        testEvent.setDescription(null);
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(savedEvent);
        assertNull(savedEvent.getDescription());

        // Event with null location
        testEvent.setDescription("Test Event");
        testEvent.setLocation(null);
        savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(savedEvent);
        assertNull(savedEvent.getLocation());

        // Event with null visibility (should default to PRIVATE)
        testEvent.setVisibility(null);
        savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(savedEvent);
        assertEquals(Visibility.PUBLIC, savedEvent.getVisibility());
    }

    @Test
    @DisplayName("Should handle extremely long text inputs")
    void testAddEvent_LongInputs() {
        // Create very long description (100KB)
        StringBuilder longDesc = new StringBuilder();
        for (int i = 0; i < 102400; i++) {
            longDesc.append("a");
        }
        testEvent.setDescription(longDesc.toString());

        // Add the event (expect the description to be truncated to 255 characters)
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));

        // Assert the description has been truncated to 255 characters
        assertNotNull(savedEvent);
        assertEquals(255, savedEvent.getDescription().length());  // Check that it is truncated to 255
        assertEquals(longDesc.substring(0, 255), savedEvent.getDescription());  // Verify the description matches the first 255 characters
    }

    @Test
    @DisplayName("Should handle special characters in event details")
    void testAddEvent_SpecialCharacters() {
        testEvent.setDescription("!@#$%^&*()_+{}|:<>?~`-=[]\\;',./");
        testEvent.setLocation("χ ψ ω Δ Σ Φ Ψ Ω");

        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(savedEvent);
        assertEquals("!@#$%^&*()_+{}|:<>?~`-=[]\\;',./", savedEvent.getDescription());
        assertEquals("χ ψ ω Δ Σ Φ Ψ Ω", savedEvent.getLocation());
    }

    @Test
    @DisplayName("Should retrieve upcoming events for user")
    void testGetUpcomingEvents() {
        // Create past, present, and future events
        Event pastEvent = new Event();
        pastEvent.setCreator(user);
        pastEvent.setTime(LocalDateTime.now().minusDays(1));
        pastEvent.setVisibility(Visibility.PUBLIC);

        assertNotNull(eventService.add(new EventDTO(pastEvent)));

        Event futureEvent = new Event();
        futureEvent.setCreator(user);
        futureEvent.setTime(LocalDateTime.now().plusDays(1));
        futureEvent.setVisibility(Visibility.PUBLIC);

        assertNotNull(eventService.add(new EventDTO(futureEvent)));

        List<EventDTO> upcomingEvents = eventService.getUpcomingEvents(user.getId());
        assertEquals(1, upcomingEvents.size());
        assertTrue(upcomingEvents.get(0).getTime().isAfter(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should handle image upload for event")
    void testAddImage() {
        testEvent.setTime(LocalDateTime.now());

        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(savedEvent);
        // Create mock image file
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        eventService.joinUser(savedEvent.getId(), user.getId());
        assertTrue(eventService.addImage(savedEvent.getId(), user.getId(), file));

        // Create second user
        RegisterDTO userData = new RegisterDTO("user2", "password", "pet", "answer");
        userService.createNewUser(userData);
        User secondUser = new User(userService.findUser(null, "user2"));

        // Test upload by non-participant (should fail)
        assertFalse(eventService.addImage(savedEvent.getId(), secondUser.getId(), file));

        // Add second user as joiner
        eventService.joinUser(savedEvent.getId(), secondUser.getId());

        // Test upload limit
        for (int i = 0; i < 5; i++) {
            eventService.addImage(savedEvent.getId(), user.getId(), file);
        }
        assertFalse(eventService.addImage(savedEvent.getId(), user.getId(), file)); // Should fail after 5 images
    }

    // Previous tests remain the same...
    @Test
    @DisplayName("Should successfully join user to event")
    void testJoinUser_Success() {
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(savedEvent);
        User secondUser = new User(userService.findUser(null, "user2"));

        assertTrue(eventService.joinUser(savedEvent.getId(), secondUser.getId()));
    }

    @Test
    @DisplayName("Should prevent duplicate user joins")
    void testJoinUser_PreventDuplicates() {
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));
        assertTrue(eventService.joinUser(savedEvent.getId(), user2.getId()));
        assertFalse(eventService.joinUser(savedEvent.getId(), user2.getId()));
    }

    @Test
    @DisplayName("Should handle extreme pagination values")
    void testGetPublicEvents_ExtremePagination() {
        // Add some events
        for (int i = 0; i < 5; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(Visibility.PUBLIC);
            event.setTime(LocalDateTime.now().plusDays(i));
            assertNotNull(eventService.add(new EventDTO(event)));
        }

        // Test with very large page size
        Page<EventDTO> largePage = eventService.getPublicEvents(PageRequest.of(0, 1000));
        assertEquals(5, largePage.getContent().size());

        // Test with very large page number
        Page<EventDTO> farPage = eventService.getPublicEvents(PageRequest.of(999, 10));
        assertTrue(farPage.getContent().isEmpty());

    }

    @Test
    @DisplayName("Should handle image upload edge cases")
    void testAddImage_EdgeCases() {
        testEvent.setTime(LocalDateTime.now());
        EventDTO savedEvent = eventService.add(new EventDTO(testEvent));

        // Test with empty file
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );
        assertFalse(eventService.addImage(savedEvent.getId(), user.getId(), emptyFile));

        // Test with very large file (10MB)
        byte[] largeContent = new byte[10 * 1024 * 1024];
        MockMultipartFile largeFile = new MockMultipartFile(
                "file",
                "large.jpg",
                "image/jpeg",
                largeContent
        );
        assertTrue(eventService.addImage(savedEvent.getId(), user.getId(), largeFile));

        // Test with wrong content type
        MockMultipartFile wrongTypeFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );
        assertFalse(eventService.addImage(savedEvent.getId(), user.getId(), wrongTypeFile));
        // Test with larger than 10 mb
        largeContent = new byte[11 * 1024 * 1024];
        largeFile = new MockMultipartFile(
                "file",
                "large.jpg",
                "image/jpeg",
                largeContent
        );
        assertFalse(eventService.addImage(savedEvent.getId(), user.getId(), largeFile));
    }

    @Test
    @DisplayName("Should handle extreme dates")
    void testAddEvent_ExtremeDates() {
        // Far future date
        testEvent.setTime(LocalDateTime.now().plusYears(1000));
        EventDTO farFutureEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(farFutureEvent);

        // Far past date
        testEvent.setTime(LocalDateTime.now().minusYears(1000));
        EventDTO farPastEvent = eventService.add(new EventDTO(testEvent));
        assertNotNull(farPastEvent);
    }

    @Test
    @DisplayName("Should handle events with same timestamp")
    void testEventsWithSameTimestamp() {
        LocalDateTime sameTime = LocalDateTime.now().plusDays(1);

        // Create multiple events with exactly same timestamp
        for (int i = 0; i < 5; i++) {
            Event event = new Event();
            event.setCreator(user);
            event.setVisibility(Visibility.PUBLIC);
            event.setTime(sameTime);
            event.setDescription("Same time event " + i);
            eventService.add(new EventDTO(event));
        }

        // Get upcoming events
        List<EventDTO> upcomingEvents = eventService.getUpcomingEvents(user.getId());
        assertEquals(5, upcomingEvents.size());

        // All events should have the same timestamp
        assertTrue(upcomingEvents.stream()
                .allMatch(e -> e.getTime().equals(sameTime)));
    }
}