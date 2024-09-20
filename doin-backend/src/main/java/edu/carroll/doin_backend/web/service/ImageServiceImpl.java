package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    public ImageServiceImpl(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }
    @Override
    public void save(Image image) {
        image.setCreatedAt(LocalDateTime.now());
        imageRepository.save(image);
    }

    @Override
    public Image get(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }
}
