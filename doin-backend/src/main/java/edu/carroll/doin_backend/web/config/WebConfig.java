package edu.carroll.doin_backend.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) in the application.
 * <p>
 * This class implements the {@link WebMvcConfigurer} interface to customize the CORS settings
 * for the application's REST API endpoints.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Adds CORS mappings for the application.
     * <p>
     * This method configures which origins are allowed to access the API,
     * the HTTP methods that can be used, allowed headers, and whether credentials can be sent.
     * </p>
     *
     * @param registry the CORS registry to customize
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply CORS settings to all API endpoints
                .allowedOrigins("http://localhost:4200") // Allow requests from this origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials to be sent with requests
    }
}