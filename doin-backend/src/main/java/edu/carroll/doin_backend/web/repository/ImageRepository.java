package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Image} entities.
 * <p>
 * This interface extends {@link JpaRepository}, providing basic CRUD operations and the ability to
 * query the database for Image entities. The primary key for Image entities is of type {@code Long}.
 * </p>
 *
 * @see JpaRepository
 * @see Image
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
}