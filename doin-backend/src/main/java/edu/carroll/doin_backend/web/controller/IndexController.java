package edu.carroll.doin_backend.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {
  static final Logger log = LoggerFactory.getLogger(IndexController.class);

  @GetMapping("/")
  public Map<String, String> index() {
    log.info("ACCESSING THE INDEX PAGE");
    Map<String, String> response = new HashMap<>();
    response.put("message", "Hello from the backend");
    return response;
  }
  
}
