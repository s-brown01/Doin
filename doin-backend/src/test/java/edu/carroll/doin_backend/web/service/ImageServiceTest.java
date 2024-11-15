package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.exception.ResourceNotFoundException;
import edu.carroll.doin_backend.web.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;


    private MockMultipartFile validImageFile;
    private MockMultipartFile validTextFile;
    private MockMultipartFile emptyFile;

    @BeforeEach
    public void setUp() {
        // Initialize test files
        validImageFile = new MockMultipartFile("file", "test-image.png", "image/png", "image data".getBytes());
        validTextFile = new MockMultipartFile("file", "test-document.txt", "text/plain", "sample text content".getBytes());
        emptyFile = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);

    }

    @Test
    public void save_ValidImageFile_Success() throws IOException {
        // Act
        Image savedImage = imageService.save(validImageFile);

        // Assert
        assertNotNull(savedImage, "Saved image should not be null");
        assertNotNull(savedImage.getId(), "Saved image ID should not be null");
        assertEquals("test-image.png", savedImage.getName(), "Image name should match the file's original name");
        assertEquals(Base64.getEncoder().encodeToString(validImageFile.getBytes()), savedImage.getData(), "Image data should be correctly encoded");

        // Check database
        assertNotNull(imageService.get(savedImage.getId()), "Image should exist in the database");
    }

    @Test
    public void save_ValidTextFile_Success() throws IOException {
        // Act
        Image savedImage = imageService.save(validTextFile);

        // Assert
        assertNotNull(savedImage, "Saved file should not be null");
        assertNotNull(savedImage.getId(), "Saved file ID should not be null");
        assertEquals("test-document.txt", savedImage.getName(), "File name should match the original name");
        assertEquals(Base64.getEncoder().encodeToString(validTextFile.getBytes()), savedImage.getData(), "File data should be correctly encoded");

        // Check database
        assertNotNull(imageService.get(savedImage.getId()), "Text file should exist in the database");
    }

    @Test
    public void save_EmptyFile_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> imageService.save(emptyFile));
        assertEquals("File is empty", exception.getMessage(), "Exception message should indicate the file is empty");
    }

    @Test
    public void save_FileWithUnsupportedType_Success() throws IOException {
        // Arrange
        MockMultipartFile unsupportedFile = new MockMultipartFile("file", "test-video.mp4", "video/mp4", "dummy video content".getBytes());

        // Act
        Image savedImage = imageService.save(unsupportedFile);

        // Assert
        assertNotNull(savedImage, "Saved file should not be null");
        assertEquals("test-video.mp4", savedImage.getName(), "Unsupported file name should match the original name");
        assertEquals(Base64.getEncoder().encodeToString(unsupportedFile.getBytes()), savedImage.getData(), "Unsupported file data should be correctly encoded");

        // Check database
        assertNotNull(imageService.get(savedImage.getId()), "Unsupported file should exist in the database");
    }

    @Test
    public void get_ExistingFile_Success() throws IOException {
        // Arrange
        Image savedImage = imageService.save(validImageFile);

        // Act
        Image retrievedImage = imageService.get(savedImage.getId());

        // Assert
        assertNotNull(retrievedImage, "Retrieved file should not be null");
        assertEquals(savedImage.getId(), retrievedImage.getId(), "File IDs should match");
        assertEquals(savedImage.getName(), retrievedImage.getName(), "File names should match");
        assertEquals(savedImage.getData(), retrievedImage.getData(), "File data should match");
    }

    @Test
    public void get_NonExistingFile() {
        Image image = imageService.get(999L);
        assertNull(image, "Retrieved file should not be null");
    }
}