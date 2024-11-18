package edu.carroll.doin_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest // This will load the main application context for tests
public class DoinBackendApplicationTests {

    @Test
    public void contextLoads() {
        // Simple test to check if the context loads correctly
        assertNotNull(this); // You can replace this with actual test logic
    }
}
