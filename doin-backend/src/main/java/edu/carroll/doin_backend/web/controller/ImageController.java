package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

/**
 * REST controller for handling image upload and retrieval operations.
 * <p>
 * This controller provides endpoints to upload images and retrieve images by their ID.
 * It uses {@link ImageService} for managing image data and operations.
 * </p>
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    /**
     * Constructs an ImageController with the specified {@link ImageService}.
     *
     * @param imageService the service used to manage images
     */
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Uploads an image file.
     * <p>
     * This endpoint accepts a multipart file and saves it as an {@link Image} entity.
     * </p>
     *
     * @param file the image file to upload
     * @return a {@link ResponseEntity} with a success message
     * @throws IOException if an I/O error occurs during file processing
     */
    @PostMapping()
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        Image img = new Image();
        img.setName(file.getOriginalFilename());
        img.setData(Base64.getEncoder().encodeToString(file.getBytes()));

        imageService.save(img);

        return ResponseEntity.ok("Image uploaded successfully!");
    }

    /**
     * Retrieves an image by its ID.
     * <p>
     * This endpoint returns the data of the requested image in Base64 format.
     * </p>
     *
     * @param id the ID of the image to retrieve
     * @return a {@link ResponseEntity} containing the image data
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getImage(@PathVariable Long id) {
        try {
            Image image = imageService.get(id);
            return ResponseEntity.ok(image.getData());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}