package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.FriendService;
import edu.carroll.doin_backend.web.service.SecurityQuestionService;
import edu.carroll.doin_backend.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LoginControllerTest {

    @Autowired
    private LoginController loginController;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityQuestionService sqService;
}
