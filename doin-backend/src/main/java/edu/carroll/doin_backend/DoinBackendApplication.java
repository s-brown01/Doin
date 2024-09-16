package edu.carroll.doin_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableJpaRepositories
public class DoinBackendApplication {
	static final Logger log = LoggerFactory.getLogger(DoinBackendApplication.class);

	public static void main(String[] args) {
		log.info("Starting DoinBackendApplication");
		SpringApplication.run(DoinBackendApplication.class, args);
	}

}
