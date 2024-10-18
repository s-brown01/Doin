package edu.carroll.doin_backend.web.config;

import edu.carroll.doin_backend.web.security.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration class for the application.
 * <p>
 * This class configures the security settings for the web application using Spring Security.
 * It includes settings for CORS, CSRF protection, and URL authorization rules.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    /**
     * Constructs a new instance of {@link SecurityConfig}.
     *
     * @param jwtTokenFilter the JWT token filter to be applied in the security chain
     */
    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    /**
     * Configures the security filter chain for the application.
     * <p>
     * This method customizes the security settings, including disabling CSRF protection,
     * setting up authorization rules for specific endpoints, and applying the JWT token filter.
     * </p>
     *
     * @param http the {@link HttpSecurity} object to customize security settings
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login").permitAll() // Allow unauthenticated access to /api/login
                        .requestMatchers("/api/register").permitAll() // Allow unauthenticated access to /api/register
                        .requestMatchers("/api/forgot-password").permitAll() // Allow unauthenticated access to /api/forgot-password
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .cors(withDefaults()) // Enable CORS support
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT token filter before username/password auth filter

        return http.build(); // Build and return the security filter chain
    }
}