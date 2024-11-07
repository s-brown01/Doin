package edu.carroll.doin_backend.web.controller;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.security.TokenService;
import edu.carroll.doin_backend.web.service.FriendService;
import edu.carroll.doin_backend.web.service.SecurityQuestionService;
import edu.carroll.doin_backend.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

@SpringBootTest
@Transactional
public class FriendControllerTest {
    private final String username1 = "username_1";
    private final String username2 = "username_2";
    private final String username3 = "username_3";
    private final String invalidUsername = "invalid username!!@@";
    private final String unusedUsername = "No_user_with_this_username";
    private String user1Header;
    private String user2Header;
    private String user3Header;
    private String invalidAuthHeader;
    private String userInvalidAuthHeader;

    @Autowired
    private FriendController friendController;

    @Autowired
    private TokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private SecurityQuestionService sqService;

    @BeforeEach
    public void setUp() {
        final String user1Token = jwtTokenService.generateToken(username1, 1);
        final String user2Token = jwtTokenService.generateToken(username2, 2);
        final String user3Token = jwtTokenService.generateToken(username3, 3);
        final String userInvalidToken = jwtTokenService.generateToken(invalidUsername, 4);
        final String invalidToken = user1Token + "FAKE TOKEN";
        user1Header = "Bearer " + user1Token;
        user2Header = "Bearer " + user2Token;
        user3Header = "Bearer " + user3Token;
        invalidAuthHeader = "Bearer " + invalidToken;
        userInvalidAuthHeader = "Bearer " + userInvalidToken;

        sqService.addSecurityQuestion("question");
        createNewUser(username1);
        createNewUser(username2);
        createNewUser(username3);
        // no users with "invalid username" or "unused username"

        // user1 and user2 are friends
        friendService.addFriend(username1, username2);
        friendService.confirmFriend(username2, username1);
        // user2 and user3 are friends
        friendService.addFriend(username2, username3);
        friendService.confirmFriend(username3, username2);
    }

    /**
     * Helper method for creating a new user with a given username and a constant password, security question,
     * and security question answer. This is used to create consistent Users in the repositories.
     *
     * @param username the username of the new user to create
     */
    private void createNewUser(String username) {
        RegisterDTO data = new RegisterDTO(username, "password", "question", "answer");
        userService.createNewUser(data);
    }

    @Test
    public void getFriendsOfFriends_Success() {
        // user1 and user3 are mutual friends
        ResponseEntity<Set<FriendshipDTO>> user1Response = friendController.getFriendsOfFriends(user1Header);
        assertEquals(ResponseEntity.ok().build().getStatusCode(), user1Response.getStatusCode(), "User1 getFriendsOfFriends should return 'OK' response");
        assertNotNull(user1Response.getBody(), "User1 getFriendsOfFriends should a hashset, never be null body");
        assertEquals(1, user1Response.getBody().size(), "User1 getFriendsOfFriends should return 1 friend of Friend");
    }

    @Test
    public void getFriendsOfFriends_Invalid() {
        final String incorrectHeader = "1"+ user1Header;
        ResponseEntity<Set<FriendshipDTO>> invalidUsernameResponse = friendController.getFriendsOfFriends(userInvalidAuthHeader);
        ResponseEntity<Set<FriendshipDTO>> invalidHeaderResponse = friendController.getFriendsOfFriends(invalidAuthHeader);
        ResponseEntity<Set<FriendshipDTO>> incorrectHeaderResponse = friendController.getFriendsOfFriends(incorrectHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, invalidUsernameResponse.getStatusCode(), "Invalid username getFriendsOfFriends should return 'UNAUTHORIZED' response");
        assertNotNull(invalidUsernameResponse.getBody(), "Invalid user getFriendsOfFriends should be not be null");
        assertTrue(invalidUsernameResponse.getBody().isEmpty(), "Invalid user getFriendsOfFriends should be empty");

        assertEquals(HttpStatus.UNAUTHORIZED, invalidHeaderResponse.getStatusCode(), "Invalid header getFriendsOfFriends should be unauthorized");
        assertNotNull(invalidHeaderResponse.getBody(), "Invalid header getFriendsOfFriends should be not be null");
        assertTrue(invalidHeaderResponse.getBody().isEmpty(), "Invalid header getFriendsOfFriends should be empty");

        assertEquals(HttpStatus.UNAUTHORIZED, incorrectHeaderResponse.getStatusCode(), "Incorrect header getFriendsOfFriends should be unauthorized");
        assertNotNull(incorrectHeaderResponse.getBody(), "Incorrect header getFriendsOfFriends should be not be null");
        assertTrue(incorrectHeaderResponse.getBody().isEmpty(), "Incorrect header getFriendsOfFriends should be empty");
    }

    @Test
    public void getFriendsOfFriends_Null() {
        ResponseEntity<Set<FriendshipDTO>> nullResponse = friendController.getFriendsOfFriends(null);
        assertEquals(HttpStatus.UNAUTHORIZED, nullResponse.getStatusCode(), "'null' User getFriendsOfFriends should return 'UNAUTHORIZED' response");
        assertNotNull(nullResponse.getBody(), "User getFriendsOfFriends should be not be null");
        assertTrue(nullResponse.getBody().isEmpty(), "User getFriendsOfFriends should be empty");
    }

    @Test
    public void getUserByUsername_Success() {
        // getting responses
        ResponseEntity<Set<FriendshipDTO>> user1toUser2 = friendController.getUserByUsername(user1Header, username2);
        ResponseEntity<Set<FriendshipDTO>> user1toUser3 = friendController.getUserByUsername(user1Header, username3);
        ResponseEntity<Set<FriendshipDTO>> unusedUsernameResponse = friendController.getUserByUsername(user1Header, unusedUsername);

        // making sure the responses are valid
        assertEquals(HttpStatus.OK, user1toUser2.getStatusCode(), "User1 getting User2 should return 'OK' response");
        assertNotNull(user1toUser2.getBody(), "User1 getting User2 should not return a null body");
        assertEquals(1, user1toUser2.getBody().size(), "User1 getting User2 should return 1 FriendshipDTO");

        assertEquals(HttpStatus.OK, user1toUser3.getStatusCode(), "User1 getting User3 should return 'OK' response");
        assertNotNull(user1toUser3.getBody(), "User1 getting User3 should not return a null body");
        assertEquals(1, user1toUser3.getBody().size(), "User1 getting User2 should return 1 FriendshipDTO");

        assertEquals(HttpStatus.OK, unusedUsernameResponse.getStatusCode(), "Valid user searching an unused username should return 'OK' status");
        assertNotNull(unusedUsernameResponse.getBody(), "Searching for an unused username should not return null body");
        assertTrue(unusedUsernameResponse.getBody().isEmpty(), "Searching for an unused username should return an empty Set");

        // checking the Friendship DTOs inside the Set
        FriendshipDTO user2Found = user1toUser2.getBody().iterator().next();
        FriendshipDTO user3Found = user1toUser3.getBody().iterator().next();
        assertNotNull(user2Found, "There should be a non-null FriendshipDTO representing User2 returned for User1");
        assertEquals(username2, user2Found.getUsername(), "username2 should be the same as the found User2");
        assertEquals(FriendshipStatus.CONFIRMED, user2Found.getStatus(), "User1 should be CONFIRMED friends with User2");

        assertNotNull(user3Found, "There should be a non-null FriendshipDTO representing User3 returned for User1");
        assertEquals(username3, user3Found.getUsername(), "username3 should be the same as the found User3");
        assertEquals(FriendshipStatus.NOTADDED, user3Found.getStatus(), "User1 should be NOT-ADDED friends with User3");
    }

    @Test
    public void getUserByUsername_Invalid() {
        ResponseEntity<Set<FriendshipDTO>> invalidHeaderResponse = friendController.getUserByUsername(userInvalidAuthHeader, username1);
        ResponseEntity<Set<FriendshipDTO>> invalidUsernameResponse = friendController.getUserByUsername(username1, invalidUsername);
        ResponseEntity<Set<FriendshipDTO>> invalidParamResponse = friendController.getUserByUsername(userInvalidAuthHeader, invalidUsername);

        assertEquals(HttpStatus.UNAUTHORIZED, invalidHeaderResponse.getStatusCode(), "An invalid header searching for valid user should return 'UNAUTHORIZED' status");
        assertEquals(HttpStatus.UNAUTHORIZED, invalidUsernameResponse.getStatusCode(), "A valid user searching for an invalid username should return 'BAD_REQUEST' status");
        assertEquals(HttpStatus.UNAUTHORIZED, invalidParamResponse.getStatusCode(), "Searching with invalid parameters should return 'UNAUTHORIZED' status");

        assertNotNull(invalidHeaderResponse.getBody(), "Searching with an invalid header should not return null body");
        assertNotNull(invalidUsernameResponse.getBody(), "Searching for an invalid username should not return null body");
        assertNotNull(invalidParamResponse.getBody(), "Searching with invalid parameters should not return null body");

        assertTrue(invalidHeaderResponse.getBody().isEmpty(), "Searching with an invalid header should return an empty Set");
        assertTrue(invalidUsernameResponse.getBody().isEmpty(), "Searching for an invalid username should return an empty Set");
        assertTrue(invalidParamResponse.getBody().isEmpty(), "Searching for an invalid parameters should return an empty Set");
    }

}
