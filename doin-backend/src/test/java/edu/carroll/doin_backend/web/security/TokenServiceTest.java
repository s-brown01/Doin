package edu.carroll.doin_backend.web.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class TokenServiceTest {
    private static final String username = "test";

    @InjectMocks
    private JwtTokenService tokenService; // Mock the TokenService

    @BeforeEach
    public void setup() {
        System.out.println("TESTING TOKEN SERVICE");
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
    }

    @Test
    public void generateToken() {
        final String mockToken = tokenService.generateToken(username);
//        when(tokenService.generateToken(username)).thenReturn(mockToken);

        assertNotNull(mockToken, "generateToken: token should not be null");

//        when(tokenService.getUsername(mockToken)).thenReturn(username);

        assertEquals(username, tokenService.getUsername(mockToken), "generateToken: username should match the token");
    }

    @Test
    public void validateToken() {
        final String mockToken = tokenService.generateToken(username);
//        when(tokenService.generateToken(username)).thenReturn(mockToken);

//        when(tokenService.getUsername(mockToken)).thenReturn(username);

        String token = tokenService.generateToken(username);
        String tokenUsername = tokenService.getUsername(token);

        assertEquals(username, tokenUsername, "validateToken: username should match the token");
    }

    /*
    private static final Logger log = LoggerFactory.getLogger(TokenServiceTest.class);

    @Autowired
    private TokenService tokenService;

    @Test
    public void generateToken() {
        log.info("generateToken - tests begin");
        final String username = "test";
        String token = tokenService.generateToken(username);
        assertNotNull("generateToken: token is null", token);

        assertTrue("generateToken: username should match", username.equals(tokenService.getUsername(token)));
    }

    @Test
    public void validateToken() {
        log.info("validateToken - tests begin");
        final String username = "test";
        String token = tokenService.generateToken(username);
        String tokenUsername = tokenService.getUsername(token);
        assertTrue("validateToken: username should match", username.equals(tokenUsername));

    }
    */
}
