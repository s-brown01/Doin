package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void save(Image image) {
        image.setCreatedAt(LocalDateTime.now());
        logger.info("Saving new image with details: {}", image);
        imageRepository.save(image);
        logger.info("Image saved successfully with ID: {}", image.getId());
    }

    @Override
    public Image get(Long id) {
        logger.info("Retrieving image with ID: {}", id);
        return imageRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Image with ID {} not found", id);
                    return new ResourceNotFoundException("Image not found");
                });
    }
}