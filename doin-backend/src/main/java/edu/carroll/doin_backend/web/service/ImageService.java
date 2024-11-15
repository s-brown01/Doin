package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The {@code ImageService} interface provides methods for managing image entities in the application.
 * It defines the contract for saving and retrieving images.
 */
public interface ImageService {

    /**
     * Saves the specified image file as a new {@link Image} entity in the data store.
     * <p>
     * The image data is encoded in Base64 format before being saved. This method also logs
     * the process of saving the image and handles potential I/O exceptions during file processing.
     * </p>
     *
     * @param file the {@link MultipartFile} representing the image to be saved.
     *             It should not be empty, and its size should be validated before saving.
     * @return the {@link Image} object representing the saved image entity.
     * @throws IOException if an I/O error occurs while reading the file data or saving the image.
     * @throws IllegalArgumentException if the provided file is empty.
     */
    Image save(MultipartFile file) throws IOException;

    /**
     * Retrieves an {@link Image} entity by its unique identifier from the data store.
     * <p>
     * If the image with the specified ID is not found, a {@link edu.carroll.doin_backend.web.exception.ResourceNotFoundException}
     * is thrown.
     * </p>
     *
     * @param id the unique identifier of the image to retrieve.
     * @return the {@link Image} object representing the retrieved image entity.
     */
    Image get(Long id);
}