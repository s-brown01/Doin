package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public Image save(MultipartFile file) throws IOException {
        logger.info("Starting to save image file with original filename: {}", file.getOriginalFilename());

        // Check if file is empty
        if (file.isEmpty()) {
            logger.warn("File is empty. Cannot save image.");
            throw new IllegalArgumentException("File is empty");
        }

        // Prepare image entity for saving
        Image img = new Image();
        img.setName(file.getOriginalFilename());
        try {
            img.setData(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            logger.error("Error reading file bytes for image: {}", e.getMessage());
            throw e;
        }
        img.setCreatedAt(LocalDateTime.now());

        logger.info("Saving new image with details: name={}, createdAt={}", img.getName(), img.getCreatedAt());

        // Save image to repository
        try {
            Image image = imageRepository.save(img);
            logger.info("Image saved successfully with ID: {}", image.getId());
            return image;
        } catch (Exception e) {
            logger.error("Failed to save image. Error: {}", e.getMessage());
            throw new IOException("Error saving image to the database", e);
        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public Image get(Long id) {
        logger.info("Retrieving image with ID: {}", id);
        Optional<Image> imgOpt = imageRepository.findById(id);
        // Return image if found, otherwise log error and return null
        if (imgOpt.isPresent()) {
            return imgOpt.get();
        }
        else {
            logger.error("Image with ID {} not found", id);
            return null;
        }
    }
}