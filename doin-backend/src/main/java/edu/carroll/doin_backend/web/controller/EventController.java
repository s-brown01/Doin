package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.model.Event;
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
    public List<Event> getAll(){
        return eventService.getAll();
    }

    @GetMapping("/{id}")
    public Event getById(@PathVariable Integer id){
        return eventService.getById(id);
    }

    @PostMapping()
    public void create(@RequestBody Event event) {
        eventService.add(event);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        eventService.delete(id);
    }

    @PutMapping
    public void update(@RequestBody Event event) {
        eventService.update(event);
    }
}
