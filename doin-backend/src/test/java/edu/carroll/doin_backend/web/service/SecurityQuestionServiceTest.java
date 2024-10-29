package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.model.SecurityQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SecurityQuestionServiceTest {
    
    @Autowired
    private SecurityQuestionService securityQuestionService;

    @Test
    public void getSecurityQuestionByValue_Successful(){
        final String validQuestion = "Valid Question";
        // make sure there's a question in the repo
        securityQuestionService.addSecurityQuestion(validQuestion);
        // get the question from the repo
        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue(validQuestion);
        assertNotNull(securityQuestion);
        assertEquals(validQuestion, securityQuestion.getQuestion());
    }

    @Test
    public void getSecurityQuestionByValue_Failure(){
        // should be no questions in repo
        final String invalidQuestion = "Invalid Question";
        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue(invalidQuestion);
        assertNull(securityQuestion);
    }

    @Test
    public void getSecurityQuestionByValue_Null(){
        // try to add, and make sure it doesn't
        securityQuestionService.addSecurityQuestion(null);
        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue(null);
        assertNull(securityQuestion);
    }

    @Test
    public void getSecurityQuestionByValue_Empty(){
        // try to add an empty string then get it
        securityQuestionService.addSecurityQuestion("");
        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue("");
        assertNull(securityQuestion);
    }

    @Test
    public void addSecurityQuestion_Successful(){
        final String validQuestion = "Valid Question";
        // make sure that is successful
        assertTrue(securityQuestionService.addSecurityQuestion(validQuestion));
        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue(validQuestion);
        assertNotNull(securityQuestion);
        assertEquals(validQuestion, securityQuestion.getQuestion());
    }

    @Test
    public void addSecurityQuestion_Failure(){
        final String question = "Invalid Question";
        // first time should be successful
        assertTrue(securityQuestionService.addSecurityQuestion(question));
        // second time should be unsuccessful
        assertFalse(securityQuestionService.addSecurityQuestion(question));
    }

    @Test
    public void addSecurityQuestion_Null(){
        // adding null should be unsuccessful
        assertFalse(securityQuestionService.addSecurityQuestion(null));
        assertNull(securityQuestionService.getSecurityQuestionByValue(null));
    }

    @Test
    public void addSecurityQuestion_Empty(){
        // adding empty-string should be unsuccessful
        assertFalse(securityQuestionService.addSecurityQuestion(""));
        assertNull(securityQuestionService.getSecurityQuestionByValue(""));
    }
}
