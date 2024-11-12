package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.enums.Visibility;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.EventType;
import edu.carroll.doin_backend.web.model.Image;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing an event.
 * <p>
 * This class is used to transfer event data between layers of the application. It contains
 * the event's id, type, visibility, creator, location, time, description, images, joiners,
 * and creation timestamp.
 * </p>
 */
public class EventDTO {

    /**
     * The unique identifier for the event.
     */
    private Integer id;

    /**
     * The type of the event (e.g., meeting, party, etc.).
     */
    private EventType eventType;

    /**
     * The visibility setting of the event (e.g., public, private).
     */
    private Visibility visibility;

    /**
     * The creator of the event.
     */
    private UserDTO creator;

    /**
     * The location where the event will take place.
     */
    private String location;

    /**
     * The scheduled time of the event.
     */
    private LocalDateTime time;

    /**
     * A description of the event.
     */
    private String description;

    /**
     * The list of images associated with the event.
     */
    private List<Image> images = new ArrayList<>();

    /**
     * The list of users who have joined the event.
     */
    private List<UserDTO> joiners = new ArrayList<>();

    /**
     * The timestamp when the event was created.
     */
    private LocalDateTime createdAt;

    /**
     * Constructs a new {@link EventDTO} from an existing {@link Event} entity.
     * <p>
     * This constructor takes an Event entity and extracts its data to populate
     * the DTO.
     * </p>
     *
     * @param event the Event entity to be transformed into a DTO.
     */
    public EventDTO(Event event) {
        this.id = event.getId();
        this.eventType = event.getEventType();
        this.visibility = event.getVisibility();
        this.creator = new UserDTO(event.getCreator());
        this.location = event.getLocation();
        this.time = event.getTime();
        this.description = event.getDescription();
        this.joiners = event.getJoiners().stream().map(UserDTO::new).toList();
        this.images = event.getImages();
        this.createdAt = event.getCreatedAt();
    }
    public EventDTO(){}

    /**
     * Gets the creation timestamp of the event.
     *
     * @return the creation timestamp of the event.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the event.
     *
     * @param createdAt the creation timestamp to set for the event.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the list of users who have joined the event.
     *
     * @return the list of users who joined the event.
     */
    public List<UserDTO> getJoiners() {
        return joiners;
    }

    /**
     * Sets the list of users who have joined the event.
     *
     * @param joiners the list of users to set as joiners of the event.
     */
    public void setJoiners(List<UserDTO> joiners) {
        this.joiners = joiners;
    }

    /**
     * Gets the list of images associated with the event.
     *
     * @return the list of images for the event.
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * Sets the list of images associated with the event.
     *
     * @param images the list of images to set for the event.
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * Gets the description of the event.
     *
     * @return the event description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the event.
     *
     * @param description the description to set for the event.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the scheduled time of the event.
     *
     * @return the time of the event.
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Sets the scheduled time of the event.
     *
     * @param time the time to set for the event.
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * Gets the location of the event.
     *
     * @return the event location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the event.
     *
     * @param location the location to set for the event.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the creator of the event.
     *
     * @return the user who created the event.
     */
    public UserDTO getCreator() {
        return creator;
    }

    /**
     * Sets the creator of the event.
     *
     * @param creator the user to set as the creator of the event.
     */
    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    /**
     * Gets the visibility setting of the event.
     *
     * @return the visibility setting of the event.
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility setting of the event.
     *
     * @param visibility the visibility to set for the event.
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Gets the type of the event (e.g., meeting, party).
     *
     * @return the event type.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Sets the type of the event.
     *
     * @param eventType the event type to set.
     */
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Gets the unique identifier of the event.
     *
     * @return the event ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the event.
     *
     * @param id the event ID to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Checks if this {@link EventDTO} object is equal to another object.
     * <p>
     * Two EventDTO objects are considered equal if all of their properties are the same.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventDTO eventDTO)) return false;
        return Objects.equals(eventType, eventDTO.eventType) && visibility == eventDTO.visibility && Objects.equals(creator, eventDTO.creator) && Objects.equals(location, eventDTO.location) && Objects.equals(time, eventDTO.time) && Objects.equals(description, eventDTO.description) && Objects.equals(images, eventDTO.images) && Objects.equals(joiners, eventDTO.joiners) && Objects.equals(createdAt, eventDTO.createdAt);
    }

    /**
     * Returns a hash code value for this {@link EventDTO} object.
     * <p>
     * The hash code is computed based on the properties of the event.
     * </p>
     *
     * @return the hash code of this EventDTO object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(eventType, visibility, creator, location, time, description, images, joiners, createdAt);
    }
}
