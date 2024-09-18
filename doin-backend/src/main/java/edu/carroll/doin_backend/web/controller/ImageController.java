package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping("/")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException, IOException {
        Image img = new Image();
        img.setName(file.getOriginalFilename());
        img.setData(Arrays.toString(file.getBytes()));

        imageRepository.save(img);

        return ResponseEntity.ok("Image uploaded successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getImage(@PathVariable Long id) {
        Image img = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        return ResponseEntity.ok().body(img.getData());
    }
}