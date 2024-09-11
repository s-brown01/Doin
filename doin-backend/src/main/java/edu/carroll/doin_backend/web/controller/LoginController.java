package edu.carroll.doin_backend.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class LoginController {
  static final Logger log = LoggerFactory.getLogger(LoginController.class);


  @GetMapping("/login")
  public String loginGet(Model model) {
    log.info("LOGIN GETTING");
    return "loginGet";
  }

  @PostMapping("/login")
  public String loginPost() {
    log.info("LOGIN POSTING");
    return "loginPost";
  }
  
}
