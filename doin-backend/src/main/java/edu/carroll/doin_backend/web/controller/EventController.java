package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    public List<EventDTO> getAll(){
        return eventService.getAll();
    }

    @GetMapping("/{id}")
    public EventDTO getById(@PathVariable Integer id){
        return eventService.getById(id);
    }

    @PostMapping()
    public EventDTO create(@RequestBody EventDTO event) {
        return eventService.add(event);
    }

    @PostMapping("/{id}/join")
    public void join(@PathVariable Integer id, Integer userId) {
        eventService.joinUser(id, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        eventService.delete(id);
    }

    @PutMapping
    public void update(@RequestBody EventDTO event) {
        eventService.update(event);
    }
}
