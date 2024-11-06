package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.SecurityQuestion;

/**
 * Service interface for managing security questions.
 * <p>
 * This interface defines methods for retrieving and adding security questions in the application.
 */
public interface SecurityQuestionService {

    /**
     * Retrieves a SecurityQuestion by its value (question text).
     * <p>
     * This method should return the security question object if the question exists and is valid.
     *
     * @param securityQuestionValue the question value to look up.
     * @return the SecurityQuestion if found, or null if not found or invalid.
     */
    SecurityQuestion getSecurityQuestionByValue(String securityQuestionValue);

    /**
     * Adds a new security question to the repository.
     * <p>
     * This method should check if the question is valid (non-null and not blank) and ensure that the question
     * doesn't already exist in the repository before adding it.
     *
     * @param questionValue the security question to add.
     * @return true if the question was successfully added, false otherwise.
     */
    boolean addSecurityQuestion(String questionValue);
}
