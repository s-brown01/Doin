package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.SecurityQuestion;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityQuestionServiceImpl implements SecurityQuestionService {
    private static final Logger log = LoggerFactory.getLogger(SecurityQuestionServiceImpl.class);

    private final SecurityQuestionRepository securityQuestionRepo;

    public SecurityQuestionServiceImpl(SecurityQuestionRepository securityQuestionRepo) {
        this.securityQuestionRepo = securityQuestionRepo;
        // make sure the repository is filled
//        this.checkAndFillRepo();
    }

    @Override
    public SecurityQuestion getSecurityQuestionByValue(String securityQuestionValue) {
        log.trace("getSecurityQuestionByValue: getting question {}", securityQuestionValue);
        final Integer id = securityQuestionRepo.findIdByQuestion(securityQuestionValue);
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

    @PostConstruct
    public void checkAndFillRepo() {
        log.trace("checkAndFillRepo: checking and populating repository");
        List<SecurityQuestion> securityQuestions = securityQuestionRepo.findAll();
        log.trace("checkAndFillRepo: found {} security questions", securityQuestions.size());
        // create all the SQ's that will be used in the repo
        SecurityQuestion petSQ = new SecurityQuestion("pet");
        SecurityQuestion schoolSQ = new SecurityQuestion("school");
        SecurityQuestion citySQ = new SecurityQuestion("city");

        log.trace("checkAndFillRepo: checking what security questions exist");
        // make sure all 3 SecurityQuestions exist in repo
        if (!securityQuestions.contains(petSQ)) {
            log.trace("checkAndFillRepo: adding security question {}", petSQ);
            securityQuestionRepo.save(petSQ);
        }
        if (!securityQuestions.contains(schoolSQ)) {
            log.trace("checkAndFillRepo: adding security question {}", schoolSQ);
            securityQuestionRepo.save(schoolSQ);
        }
        if (!securityQuestions.contains(citySQ)) {
            log.trace("checkAndFillRepo: adding security question {}", citySQ);
            securityQuestionRepo.save(citySQ);
        }
        log.trace("checkAndFillRepo: security questions repo filled with {} questions", securityQuestions.size());

    }

}
