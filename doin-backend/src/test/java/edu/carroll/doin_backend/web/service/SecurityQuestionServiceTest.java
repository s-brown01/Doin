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
    public void getSecurityQuestionByValue_Failure(){
        // should be no questions in repo
        //modify NAME of question
        final String invalidQuestion = "Non-existent question";
        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue(invalidQuestion);
        assertNull(securityQuestion);
    }

    @Test
    public void getSecurityQuestionByValue_Successful(){
        final String validQuestion = "Question";
        // make sure there's a question in the repo
        assertTrue(securityQuestionService.addSecurityQuestion(validQuestion));
        // get the question from the repo
        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue(validQuestion);
        assertNotNull(securityQuestion);
        assertEquals(validQuestion, securityQuestion.getQuestion());
    }

    @Test
    public void getSecurityQuestionByValue_MoreThanOne(){
        final String question1 = "Question1";
        final String question2 = "Question2";

        // make sure there's a question in the repo
        assertTrue(securityQuestionService.addSecurityQuestion(question1));
        assertTrue(securityQuestionService.addSecurityQuestion(question2));

        // get the questions from the repo
        SecurityQuestion securityQuestion1 = securityQuestionService.getSecurityQuestionByValue(question1);
        assertNotNull(securityQuestion1);
        assertEquals(question1, securityQuestion1.getQuestion());

        SecurityQuestion securityQuestion2 = securityQuestionService.getSecurityQuestionByValue(question2);
        assertNotNull(securityQuestion2);
        assertEquals(question2, securityQuestion2.getQuestion());
    }

    @Test
    public void getSecurityQuestionByValue_Null(){
        final String validQuestion = "Question";
        // make sure there's a question in the repo
        assertTrue(securityQuestionService.addSecurityQuestion(validQuestion));

        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue(null);
        assertNull(securityQuestion);
    }

    @Test
    public void getSecurityQuestionByValue_Empty(){
        final String validQuestion = "Question";
        // make sure there's a question in the repo
        assertTrue(securityQuestionService.addSecurityQuestion(validQuestion));

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
        final String question = "Duplicated question";
        // first time should be successful
        assertTrue(securityQuestionService.addSecurityQuestion(question));
        // second time should be unsuccessful
        assertFalse(securityQuestionService.addSecurityQuestion(question));
    }
    //MORE THAN ONE QUESTION
    @Test
    public void addSecurityQuestion_MoreThanOne(){
        final String question1 = "Question1";
        final String question2 = "Question2";
        final String question3 = "Question3";


        // make sure there's a question in the repo
        assertTrue(securityQuestionService.addSecurityQuestion(question1));
        assertTrue(securityQuestionService.addSecurityQuestion(question2));
        assertTrue(securityQuestionService.addSecurityQuestion(question3));


        // get the questions from the repo
        SecurityQuestion securityQuestion1 = securityQuestionService.getSecurityQuestionByValue(question1);
        assertNotNull(securityQuestion1);
        assertEquals(question1, securityQuestion1.getQuestion());

        SecurityQuestion securityQuestion2 = securityQuestionService.getSecurityQuestionByValue(question2);
        assertNotNull(securityQuestion2);
        assertEquals(question2, securityQuestion2.getQuestion());

        SecurityQuestion securityQuestion3 = securityQuestionService.getSecurityQuestionByValue(question3);
        assertNotNull(securityQuestion3);
        assertEquals(question3, securityQuestion3.getQuestion());
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

    @Test
    public void addSecurityQuestion_SpecialCharacters_Successful() {
        // Test with a question containing special characters
        final String specialCharQuestion = "What's your favorite movie? 🎥🍿 & Why?";

        // Ensure that adding a question with special characters is successful
        assertTrue(securityQuestionService.addSecurityQuestion(specialCharQuestion));

        // Retrieve the added security question and verify its content
        SecurityQuestion securityQuestion = securityQuestionService.getSecurityQuestionByValue(specialCharQuestion);
        assertNotNull(securityQuestion);
        assertEquals(specialCharQuestion, securityQuestion.getQuestion());
    }
}
