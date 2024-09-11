package edu.carroll.doin_backend.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class IndexController {
  static final Logger log = LoggerFactory.getLogger(IndexController.class);

  @GetMapping("/")
  public String index() {
    log.info("ACCESSING THE INDEX PAGE");
    return "this is the index page";
  }
  
}
