package edu.carroll.doin_backend.web.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.EventRepository;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.service.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class EventServiceImplTest {

    private LoginRepository userRepository;

    @Autowired
    private EventServiceImpl eventService;

    @BeforeEach
    public void setup() {
    }

    @Test
    void testAddEvent() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        Event event = new Event();
        event.setId(1);
        event.setCreator(user);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDTO savedEvent = eventService.add(new EventDTO(event));

        assertNotNull(savedEvent);
        assertEquals(1, savedEvent.getId());
    }

    @Test
    void testGetAllEvents() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");

        Event event1 = new Event();
        event1.setId(1);
        event1.setCreator(user1);

        Event event2 = new Event();
        event2.setId(2);
        event2.setCreator(user2);

        List<Event> events = Arrays.asList(event1, event2);

        when(eventRepository.findAll()).thenReturn(events);

//        List<EventDTO> retrievedEvents = eventService.getAll();

//        assertNotNull(retrievedEvents);
//        assertEquals(2, retrievedEvents.size());
//        assertEquals(new EventDTO(event1), retrievedEvents.get(0));
//        assertEquals(new EventDTO(event2), retrievedEvents.get(1));
//        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Success() {
        Event event = new Event();
        event.setId(1);
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");
        event.setCreator(user1);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        EventDTO result = eventService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetById_EventNotFound() {
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.getById(1));
        verify(eventRepository, times(1)).findById(1);
    }
}