package edu.carroll.doin_backend.web.model;

import jakarta.persistence.*;

/**
 * Represents an event type in the system.
 * <p>
 * This entity maps to the "event_types" table in the database and stores information about the type of events
 * that can be created within the system. Each event type has a unique identifier and a name.
 * </p>
 */
@Entity
@Table(name = "event_types")
public class EventType {
    //one to many consider
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    /**
     * Retrieves the unique identifier of the event type.
     *
     * @return the ID of the event type
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the event type.
     *
     * @param id the new ID to set for the event type
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
