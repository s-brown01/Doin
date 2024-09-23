package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.enums.Visibility;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.EventType;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDTO {
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<UserDTO> getJoiners() {
        return joiners;
    }

    public void setJoiners(List<UserDTO> joiners) {
        this.joiners = joiners;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;
    private EventType eventType;
    private Visibility visibility;
    private UserDTO creator;
    private String location;
    private LocalDateTime time;
    private String description;
    private List<Image> images = new ArrayList<>();
    private List<UserDTO> joiners = new ArrayList<>();
    private LocalDateTime createdAt;

    public EventDTO(Event event) {
        this.id = event.getId();
        this.eventType = event.getEventType();
        this.visibility = event.getVisibility();
        this.creator = new UserDTO(event.getCreator());
        this.location = event.getLocation();
        this.time = event.getTime();
        this.description = event.getDescription();
        this.images = event.getImages();
    }
    public EventDTO() {}
}
