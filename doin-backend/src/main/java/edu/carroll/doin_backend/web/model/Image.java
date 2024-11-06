package edu.carroll.doin_backend.web.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents an image stored in the system.
 * <p>
 * This entity contains information about an image, including its name, data (image content),
 * and the timestamp when the image was created. The image data is stored as a large text field (LONGTEXT).
 * </p>
 */
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String data;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Gets the unique identifier for the image.
     *
     * @return the ID of the image.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the image.
     *
     * @param id the ID to set for the image.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the image.
     *
     * @return the name of the image.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the image.
     *
     * @param name the name to set for the image.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the image data (content) as a string.
     * <p>
     * The image data is stored as a large text field (LONGTEXT) and may contain a base64-encoded representation
     * of the image content.
     * </p>
     *
     * @return the image data.
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the image data (content).
     * <p>
     * The image data should be a base64-encoded string representing the image content.
     * </p>
     *
     * @param data the image data to set.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Gets the creation timestamp of the image.
     *
     * @return the timestamp when the image was created.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the image.
     *
     * @param createdAt the timestamp to set.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
