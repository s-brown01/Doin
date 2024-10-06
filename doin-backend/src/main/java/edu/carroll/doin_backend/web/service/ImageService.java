package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.Image;

/**
 * The {@code ImageService} interface provides methods for managing image entities in the application.
 * It defines the contract for saving and retrieving images.
 */
public interface ImageService {

    /**
     * Saves the specified image entity to the data store.
     *
     * @param image the {@link Image} object to be saved.
     */
    void save(Image image);

    /**
     * Retrieves an image entity by its unique identifier.
     *
     * @param id the unique identifier of the image to retrieve.
     * @return the {@link Image} object representing the retrieved image.
     */
    Image get(Long id);
}