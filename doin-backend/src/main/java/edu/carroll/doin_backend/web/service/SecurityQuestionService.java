package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.ValidateResult;
import edu.carroll.doin_backend.web.model.SecurityQuestion;
import org.springframework.stereotype.Service;

public interface SecurityQuestionService {
//    Integer findIDBySecurityQuestionValue(String securityQuestionValue);
//    SecurityQuestion findSecurityQuestionByID(Integer securityQuestionID);
    SecurityQuestion getSecurityQuestionByValue(String securityQuestionValue);
//    ValidateResult validateAndGetSecurityQuestionByValue(String securityQuestionValue);
}
