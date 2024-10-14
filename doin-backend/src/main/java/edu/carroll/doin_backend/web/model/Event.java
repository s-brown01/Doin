package edu.carroll.doin_backend.web.model;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.enums.Visibility;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column
    private String location;

    @Column
    private LocalDateTime time;

    @Column
    private String description;

    @ManyToMany
    @JoinTable(
            name = "event_images",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "event_joiners",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> joiners = new ArrayList<>();

    @Column
    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<User> getJoiners() {
        return joiners;
    }

    public void setJoiners(List<User> joiners) {
        this.joiners = joiners;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void addImages(List<Image> images){
        this.images.addAll(images);
    }

    public void addJoiner(User user){
        this.joiners.add(user);
    }

    public Event(EventDTO event) {
        this.id = event.getId();
        this.eventType = event.getEventType();
        this.visibility = event.getVisibility();
        this.creator = new User(event.getCreator());
        this.location = event.getLocation();
        this.time = event.getTime();
        this.description = event.getDescription();
        this.images = event.getImages();
    }
    public Event() {}
}