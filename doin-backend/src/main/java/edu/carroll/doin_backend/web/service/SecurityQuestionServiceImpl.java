package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.SecurityQuestion;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityQuestionServiceImpl implements SecurityQuestionService {
    private final Logger log = LoggerFactory.getLogger(SecurityQuestionServiceImpl.class);

    private final SecurityQuestionRepository securityQuestionRepo;

    public SecurityQuestionServiceImpl(SecurityQuestionRepository securityQuestionRepo) {
        this.securityQuestionRepo = securityQuestionRepo;
        // make sure the repository is filled
        this.checkAndFillRepo();
    }

    @Override
    public SecurityQuestion getSecurityQuestionByValue(String securityQuestionValue) {
        return null;
    }

    public void checkAndFillRepo() {
        List<SecurityQuestion> securityQuestions = securityQuestionRepo.findAll();
        // create all the SQ's that will be used in the repo
        SecurityQuestion petSQ = new SecurityQuestion("pet");
        SecurityQuestion schoolSQ = new SecurityQuestion("school");
        SecurityQuestion citySQ = new SecurityQuestion("city");

        // make sure all 3 SecurityQuestions exist in repo
        if (!securityQuestions.contains(petSQ)) {
            securityQuestionRepo.save(petSQ);
        }
        if (!securityQuestions.contains(schoolSQ)) {
            securityQuestionRepo.save(schoolSQ);
        }
        if (!securityQuestions.contains(citySQ)) {
            securityQuestionRepo.save(citySQ);
        }
    }

}
