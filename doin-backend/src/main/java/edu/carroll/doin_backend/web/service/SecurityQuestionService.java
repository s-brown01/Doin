package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.SecurityQuestion;

public interface SecurityQuestionService {
    SecurityQuestion getSecurityQuestionByValue(String securityQuestionValue);
}
