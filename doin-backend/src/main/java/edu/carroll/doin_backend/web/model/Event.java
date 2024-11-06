package edu.carroll.doin_backend.web.model;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.enums.Visibility;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event in the system.
 * <p>
 * This entity maps to the "events" table in the database and stores details related to an event, including its type,
 * visibility, creator, location, time, description, images, and joiners. It also contains functionality to manage the
 * creation and joining of events, as well as the addition of images.
 * </p>
 * <p>
 * This class is used to model events that can be created, updated, and viewed within the application.
 * </p>
 *
 * @see EventType
 * @see User
 * @see Image
 * @see Visibility
 */
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

    /**
     * Constructs an event from the provided {@link EventDTO}.
     * <p>
     * This constructor maps the properties from the provided {@link EventDTO} to the fields of the event entity.
     * </p>
     *
     * @param event a {@link EventDTO} object that contains the data for the event
     */
    public Event(EventDTO event) {
        this.id = event.getId();
        this.eventType = event.getEventType();
        this.visibility = event.getVisibility();
        this.creator = new User(event.getCreator());
        this.location = event.getLocation();
        this.time = event.getTime();
        this.description = event.getDescription();
        this.images = event.getImages();
        createdAt = event.getCreatedAt();
    }

    /**
     * Default constructor for the {@link Event} class.
     * <p>
     * This constructor creates an instance of the {@link Event} class without setting any fields.
     * </p>
     */
    public Event() {
    }

    /**
     * Retrieves the unique identifier of the event.
     *
     * @return the ID of the event
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the event.
     *
     * @param id the new ID to set for the event
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retrieves the event type for this event.
     *
     * @return the {@link EventType} associated with the event
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Sets the event type for this event.
     *
     * @param eventType the {@link EventType} to set for the event
     */
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Retrieves the visibility setting of the event.
     *
     * @return the {@link Visibility} of the event
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility for the event.
     *
     * @param visibility the {@link Visibility} to set for the event
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Retrieves the creator (user) of the event.
     *
     * @return the {@link User} who created the event
     */
    public User getCreator() {
        return creator;
    }

    /**
     * Sets the creator (user) for the event.
     *
     * @param creator the {@link User} who will be set as the creator of the event
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * Retrieves the location of the event.
     *
     * @return the location as a {@link String} of the event
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the event.
     *
     * @param location the new location for the event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the date and time the event will take place.
     *
     * @return the {@link LocalDateTime} representing the time of the event
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Sets the time for the event.
     *
     * @param time the new time to set for the event
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * Retrieves the description of the event.
     *
     * @return the {@link String} description of the event
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the event.
     *
     * @param description the new description to set for the event
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the list of images associated with the event.
     *
     * @return a {@link List} of {@link Image} objects linked to the event
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * Sets the list of images for the event.
     *
     * @param images a {@link List} of {@link Image} objects to associate with the event
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * Retrieves the list of users who have joined the event.
     *
     * @return a {@link List} of {@link User} objects who are participants in the event
     */
    public List<User> getJoiners() {
        return joiners;
    }

    /**
     * Sets the list of users who have joined the event.
     *
     * @param joiners a {@link List} of {@link User} objects to set as joiners for the event
     */
    public void setJoiners(List<User> joiners) {
        this.joiners = joiners;
    }

    /**
     * Retrieves the date and time when the event was created.
     *
     * @return the {@link LocalDateTime} when the event was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the date and time when the event was created.
     *
     * @param createdAt the {@link LocalDateTime} to set as the creation date for the event
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Adds a list of images to the event.
     * <p>
     * This method allows the addition of multiple images to the event by appending them to the existing list of images.
     * </p>
     *
     * @param images a {@link List} of {@link Image} objects to add to the event
     */
    public void addImages(List<Image> images) {
        this.images.addAll(images);
    }

    /**
     * Adds a user as a joiner (participant) of the event.
     * <p>
     * This method adds a user to the list of joiners for the event.
     * </p>
     *
     * @param user the {@link User} to add as a joiner for the event
     */
    public void addJoiner(User user) {
        this.joiners.add(user);
    }

    /**
     * Pre-persistence method that sets the creation date and time of the event before it is saved to the database.
     * <p>
     * This method is automatically invoked by JPA before the event entity is persisted, ensuring that the creation timestamp
     * is properly set.
     * </p>
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}