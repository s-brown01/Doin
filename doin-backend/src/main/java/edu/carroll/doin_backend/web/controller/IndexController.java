package edu.carroll.doin_backend.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
  @GetMapping("/")
  public String index() {
    return "this is the index page";
  }
  
}
