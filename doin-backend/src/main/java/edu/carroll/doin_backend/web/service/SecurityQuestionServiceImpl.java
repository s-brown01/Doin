package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.SecurityQuestion;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SecurityQuestionServiceImpl implements SecurityQuestionService {
    private static final Logger log = LoggerFactory.getLogger(SecurityQuestionServiceImpl.class);

    private final SecurityQuestionRepository securityQuestionRepo;

    public SecurityQuestionServiceImpl(SecurityQuestionRepository securityQuestionRepo) {
        this.securityQuestionRepo = securityQuestionRepo;
    }

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
                log.warn("getSecurityQuestionByValue: question ({}) does not mach input value {}", securityQuestion.getQuestion(), securityQuestionValue);
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

    private void checkAndFillRepo() {
        log.trace("checkAndFillRepo: checking and populating repository");
        addSecurityQuestion("pet");
        addSecurityQuestion("city");
        addSecurityQuestion("school");
        log.trace("checkAndFillRepo: security questions repo filled with {} questions", securityQuestionRepo.count());

    }

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
        // if not null and not exist, create the new SecurityQuestion
        log.info("addSecurityQuestion: adding question {}", questionValue);
        securityQuestionRepo.save(new SecurityQuestion(questionValue));
        return true;
    }

}
