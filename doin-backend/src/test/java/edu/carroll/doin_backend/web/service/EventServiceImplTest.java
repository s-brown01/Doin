package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import edu.carroll.doin_backend.web.enums.Visibility;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class EventServiceImplTest {
    @Autowired
    private EventService eventService;

    private Event event;
    private EventDTO eventDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("hashedpassword");

        event = new Event();
        event.setDescription("Test Event");
        event.setLocation("Test Location");
        event.setVisibility(Visibility.PUBLIC);
        event.setCreator(user);

        eventDTO = new EventDTO();
        eventDTO.setDescription("Test Event");
        eventDTO.setLocation("Test Location");
        eventDTO.setCreator(new UserDTO(user));

    }

    @Test
    @Rollback
    public void testGetAllEvents() {
        List<EventDTO> allEvents = eventService.getAll();
        assertEquals(1, allEvents.size());
        assertEquals("Test Event", allEvents.get(0).getDescription());
    }

    @Test
    @Rollback
    public void testAddEvent() {

        EventDTO savedEvent = eventService.add(eventDTO);

        assertNotNull(savedEvent.getId());
        assertEquals(eventDTO, savedEvent);

        List<EventDTO> allEvents = eventService.getAll();
        assertEquals(1, allEvents.size());
    }

    @Test
    @Rollback
    public void testUpdateEvent() {
        eventDTO.setDescription("Updated Event");
        eventService.update(eventDTO);
    }

    @Test
    @Rollback
    public void testDeleteEvent() {
        eventService.delete(event.getId());
    }
}