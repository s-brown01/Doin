package edu.carroll.doin_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class DoinBackendApplication {
	static final Logger log = LoggerFactory.getLogger(DoinBackendApplication.class);

	public static void main(String[] args) {
		log.trace("Starting application");
		SpringApplication.run(DoinBackendApplication.class, args);
	}

}
