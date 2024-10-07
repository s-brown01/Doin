package edu.carroll.doin_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@SpringBootApplication
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DoinBackendApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(DoinBackendApplicationTests.class);

    public static void main(String[] args) {
        log.info("Starting DoinBackendApplication");
        SpringApplication.run(DoinBackendApplicationTests.class, args);
    }

}

