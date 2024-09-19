package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.Image;
import org.springframework.stereotype.Service;

public interface ImageService {
    void save(Image image);
    Image get(Long id);
}
