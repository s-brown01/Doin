package edu.carroll.doin_backend.web.security;

import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;

/**
 * Filter for processing JWT authentication tokens.
 * <p>
 * This filter checks the presence of a JWT in the Authorization header, validates it,
 * and sets the authentication in the SecurityContext if the token is valid.
 * </p>
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenUtil;
    private final LoginRepository loginRepository;

    /**
     * Constructs a new instance of {@link JwtTokenFilter}.
     *
     * @param jwtTokenUtil   the service for handling JWT operations
     * @param loginRepository the repository for accessing user login data
     */
    public JwtTokenFilter(JwtTokenService jwtTokenUtil,
                          LoginRepository loginRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.loginRepository = loginRepository;
    }

    /**
     * Filters requests to process JWT authentication.
     * <p>
     * This method extracts the JWT token from the Authorization header,
     * validates it, and if valid, retrieves the associated user details
     * from the repository and sets the authentication context.
     * </p>
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param chain    the filter chain for further processing
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();

        // Allow unauthenticated access to register and login and forgot-password endpoints
        if (uri.contains("/api/register") || uri.contains("/api/login") || uri.contains("/api/forgot-password")) {
            chain.doFilter(request, response);
            return;
        }
//        if ("/api/register".equals(uri) || "/api/login".equals(uri) || "/api/forgot-password".equals(uri)) {
//            chain.doFilter(request, response);
//            return;
//        }

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the Authorization header is present and valid
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();

        // Validate the JWT token
        if (!jwtTokenUtil.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        // Retrieve user details from the repository
        List<User> userDetailsList = loginRepository
                .findByUsernameIgnoreCase(jwtTokenUtil.getUsername(token));

        User userDetails = (userDetailsList != null && !userDetailsList.isEmpty())
                ? userDetailsList.get(0)
                : null;

        // If no user details found, continue the filter chain
        if (userDetails == null) {
            chain.doFilter(request, response);
            return;
        }

        // Create authentication token and set it in the security context
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, List.of());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}