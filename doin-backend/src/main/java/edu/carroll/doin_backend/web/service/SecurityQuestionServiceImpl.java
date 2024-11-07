package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.SecurityQuestion;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing SecurityQuestions.
 * <p>
 * This service provides functionality to retrieve and add security questions to the repository.
 * It includes logging at various levels to provide insights into the process.
 */
@Service
public class SecurityQuestionServiceImpl implements SecurityQuestionService {
    /**
     * A {@link Logger} to just for this class
     */
    private static final Logger log = LoggerFactory.getLogger(SecurityQuestionServiceImpl.class);

    private final SecurityQuestionRepository securityQuestionRepo;

    /**
     * Constructor for SecurityQuestionServiceImpl.
     *
     * @param securityQuestionRepo the repository for performing CRUD operations on SecurityQuestions.
     */
    public SecurityQuestionServiceImpl(SecurityQuestionRepository securityQuestionRepo) {
        this.securityQuestionRepo = securityQuestionRepo;
    }

    /**
     * Retrieves a SecurityQuestion by its value (question text).
     * <p>
     * This method first checks if the provided question value is valid. Then, it looks up the question in the repository by its ID.
     * If found, it checks if the question matches the input value and returns it.
     *
     * @param securityQuestionValue the question value to look up.
     * @return the SecurityQuestion if found and validated, or null if not found or invalid.
     */
    @Override
    public SecurityQuestion getSecurityQuestionByValue(String securityQuestionValue) {
        log.trace("getSecurityQuestionByValue: starting for question value {}", securityQuestionValue);
        if (securityQuestionValue == null || securityQuestionValue.isBlank()) {
            log.warn("getSecurityQuestionByValue: securityQuestionValue is null or blank");
            return null;
        }
        log.trace("getSecurityQuestionByValue: getting question {}", securityQuestionValue);
        final Integer id = securityQuestionRepo.findIdByQuestion(securityQuestionValue);
        log.debug("getSecurityQuestionByValue: getting question {}", id);
        if (id == null) {
            log.warn("getSecurityQuestionByValue: could not find question {}", securityQuestionValue);
            return null;
        }
        log.trace("getSecurityQuestionByValue: found question {} with id {}", securityQuestionValue, id);
        try {
            log.trace("getSecurityQuestionByValue: getting security question {}", securityQuestionValue);
            final SecurityQuestion securityQuestion = securityQuestionRepo.getReferenceById(id);
            if (securityQuestion == null || securityQuestion.getQuestion() == null) {
                log.warn("getSecurityQuestionByValue: could not find question {}", securityQuestionValue);
                return null;
            }
            if (!securityQuestion.getQuestion().equals(securityQuestionValue)) {
                log.warn("getSecurityQuestionByValue: question ({}) does not match input value {}", securityQuestion.getQuestion(), securityQuestionValue);
                return null;
            }
            log.trace("getSecurityQuestionByValue: found and validated question {}", securityQuestionValue);
            return securityQuestion;
        } catch (EntityNotFoundException e) {
            log.error("getSecurityQuestionByValue: could not find the entity for security question {}", securityQuestionValue);
            return null;
        } catch (Exception e) {
            log.error("getSecurityQuestionByValue: Error {} occurred while trying to get security question {}", e.getMessage(), securityQuestionValue);
            return null;
        }
    }

    /**
     * Adds a new security question to the repository.
     * <p>
     * The method validates the input question by checking if it's null or blank, and if it already exists in the repository.
     * If the question is valid, it is saved to the repository.
     *
     * @param questionValue the security question to add.
     * @return true if the question was successfully added, false otherwise.
     */
    @Override
    public boolean addSecurityQuestion(String questionValue) {
        log.trace("addSecurityQuestion: validating question {}", questionValue);
        // make sure new question isn't null or blank
        if (questionValue == null || questionValue.isBlank()) {
            return false;
        }
        // make sure question doesn't already exist
        if (securityQuestionRepo.existsByQuestion(questionValue)) {
            log.warn("addSecurityQuestion: question {} already exists", questionValue);
            return false;
        }
        // if not null and does not exist, create the new SecurityQuestion
        log.info("addSecurityQuestion: adding question {}", questionValue);
        securityQuestionRepo.save(new SecurityQuestion(questionValue));
        return true;
    }
}
