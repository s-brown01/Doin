package edu.carroll.doin_backend;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import edu.carroll.doin_backend.service.LoginServiceImpl;

@WebMvcTest(LoginServiceImpl.class)
public class LoginServiceTest {

}
