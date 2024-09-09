package edu.carroll.doin_backend.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
  @GetMapping("/login")
  public String loginGet(Model model) {
    return "loginGet";
  }

  @PostMapping("/login")
  public String loginPost() {
    return "loginPost";
  }
  
}
