package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.model.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for interacting with {@link SecurityQuestion} entities.
 * <p>
 * Provides methods for querying, checking existence, and retrieving specific details of security questions
 * used for user authentication and password recovery.
 * </p>
 *
 * @see SecurityQuestion
 */
public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Integer> {

    /**
     * Finds a list of {@link SecurityQuestion} entities by their question text.
     * <p>
     * Returns a list of questions matching the provided text.
     * </p>
     *
     * @param question the question text to search for
     * @return a list of {@link SecurityQuestion} objects that match the question text
     */
    List<SecurityQuestion> findByQuestion(String question);

    /**
     * Retrieves the ID of the {@link SecurityQuestion} based on the provided question text.
     * <p>
     * Returns the ID of the question or {@code null} if not found.
     * </p>
     *
     * @param question the question text to search for
     * @return the ID of the matching {@link SecurityQuestion}, or {@code null} if not found
     */
    @Query("SELECT sq.id FROM SecurityQuestion sq WHERE sq.question = :question")
    Integer findIdByQuestion(@Param("question") String question);

    /**
     * Checks if a {@link SecurityQuestion} with the specified question text exists.
     * <p>
     * Returns {@code true} if a question with the given text exists, otherwise {@code false}.
     * </p>
     *
     * @param question the question text to check for existence
     * @return {@code true} if the question exists, {@code false} otherwise
     */
    boolean existsByQuestion(String question);
}
