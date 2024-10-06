package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The {@code ImageServiceImpl} class is an implementation of the {@link ImageService} interface.
 * It provides methods for saving and retrieving image entities from the data store.
 */
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    /**
     * Constructs an {@code ImageServiceImpl} instance with the specified image repository.
     *
     * @param imageRepository the {@link ImageRepository} used for image data access.
     */
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Saves the specified image entity to the data store.
     * The creation timestamp is set to the current time before saving.
     *
     * @param image the {@link Image} object to be saved.
     */
    @Override
    public void save(Image image) {
        image.setCreatedAt(LocalDateTime.now());
        imageRepository.save(image);
    }

    /**
     * Retrieves an image entity by its unique identifier.
     *
     * @param id the unique identifier of the image to retrieve.
     * @return the {@link Image} object representing the retrieved image.
     * @throws ResourceNotFoundException if no image with the specified ID exists.
     */
    @Override
    public Image get(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }
}