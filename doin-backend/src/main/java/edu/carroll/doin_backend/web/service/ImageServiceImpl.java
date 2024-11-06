package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image save(MultipartFile file) throws IOException {
        Image img = new Image();
        img.setName(file.getOriginalFilename());
        img.setData(Base64.getEncoder().encodeToString(file.getBytes()));
        img.setCreatedAt(LocalDateTime.now());
        logger.info("Saving new image with details: {}", img);
        Image image = imageRepository.save(img);
        logger.info("Image saved successfully with ID: {}", image.getId());
        return image;
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