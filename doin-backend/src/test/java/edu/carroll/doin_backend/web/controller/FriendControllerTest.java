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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
    private String invalidUserAuthHeader;
    private String incorrectHeader;


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
        sqService.addSecurityQuestion("question");
        createNewUser(username1);
        createNewUser(username2);
        createNewUser(username3);
        // no users with "invalid username" or "unused username"

        final String user1Token = jwtTokenService.generateToken(username1, userService.findUser(null, username1).getId());
        final String user2Token = jwtTokenService.generateToken(username2, userService.findUser(null, username2).getId());
        final String user3Token = jwtTokenService.generateToken(username3, userService.findUser(null, username3).getId());
        final String invalidUserToken = jwtTokenService.generateToken(invalidUsername, 1000000);
        final String invalidToken = user1Token + "FAKE TOKEN";
        user1Header = "Bearer " + user1Token;
        user2Header = "Bearer " + user2Token;
        user3Header = "Bearer " + user3Token;
        invalidAuthHeader = "Bearer " + invalidToken;
        invalidUserAuthHeader = "Bearer " + invalidUserToken;
        incorrectHeader = "incorrect" + user1Header;

        // user1 and user2 are friends
        assertTrue(friendService.addFriend(username1, username2).isValid());
        assertTrue(friendService.confirmFriend(username2, username1).isValid());
        // user2 and user3 are friends
        assertTrue(friendService.addFriend(username2, username3).isValid());
        assertTrue(friendService.confirmFriend(username3, username2).isValid());
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
    public void getFriendsOfFriends_InvalidUserAuthHeader() {
        ResponseEntity<Set<FriendshipDTO>> invalidUsernameResponse = friendController.getFriendsOfFriends(invalidUserAuthHeader);
        assertEquals(HttpStatus.UNAUTHORIZED, invalidUsernameResponse.getStatusCode(), "Invalid username getFriendsOfFriends should return 'UNAUTHORIZED' response");
        assertNotNull(invalidUsernameResponse.getBody(), "Invalid user getFriendsOfFriends should be not be null");
        assertTrue(invalidUsernameResponse.getBody().isEmpty(), "Invalid user getFriendsOfFriends should be empty");
    }

    @Test
    public void getFriendsOfFriends_InvalidHeader() {
        ResponseEntity<Set<FriendshipDTO>> invalidHeaderResponse = friendController.getFriendsOfFriends(invalidAuthHeader);
        assertEquals(HttpStatus.UNAUTHORIZED, invalidHeaderResponse.getStatusCode(), "Invalid header getFriendsOfFriends should be unauthorized");
        assertNotNull(invalidHeaderResponse.getBody(), "Invalid header getFriendsOfFriends should be not be null");
        assertTrue(invalidHeaderResponse.getBody().isEmpty(), "Invalid header getFriendsOfFriends should be empty");
    }

    @Test
    public void getFriendsOfFriends_IncorrectHeader() {
        ResponseEntity<Set<FriendshipDTO>> incorrectHeaderResponse = friendController.getFriendsOfFriends(incorrectHeader);
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
    public void getUserByUsername_OneResult() {
        // getting responses
        ResponseEntity<Set<FriendshipDTO>> user1toUser2 = friendController.getUserByUsername(user1Header, username2);

        // making sure the responses are valid
        assertEquals(HttpStatus.OK, user1toUser2.getStatusCode(), "User1 getting User2 should return 'OK' response");
        assertNotNull(user1toUser2.getBody(), "User1 getting User2 should not return a null body");
        assertEquals(1, user1toUser2.getBody().size(), "User1 getting User2 should return 1 FriendshipDTO");

        // checking the Friendship DTOs inside the Set
        FriendshipDTO user2Found = user1toUser2.getBody().iterator().next();
        assertNotNull(user2Found, "There should be a non-null FriendshipDTO representing User2 returned for User1");
        assertEquals(username2, user2Found.getUsername(), "username2 should be the same as the found User2");
        assertEquals(FriendshipStatus.CONFIRMED, user2Found.getStatus(), "User1 should be CONFIRMED friends with User2");
    }

    @Test
    public void getUserByUsername_ManyResults() {
        ResponseEntity<Set<FriendshipDTO>> user1Search = friendController.getUserByUsername(user1Header, "user");
        assertEquals(HttpStatus.OK, user1Search.getStatusCode(), "User1 getting User2 should return 'OK' response");
        assertNotNull(user1Search.getBody(), "User1 getting User2 should not return a null body");
        assertEquals(3, user1Search.getBody().size(), "User1 searching for 'user' should return 3 users");
        boolean containsUser1 = false;
        boolean containsUser2 = false;
        boolean containsUser3 = false;
        for (FriendshipDTO friend : user1Search.getBody()) {
            if (friend.getUsername().equals(username1)) {
                containsUser1 = true;
                assertEquals(FriendshipStatus.IS_SELF, friend.getStatus(), "User1's status with themselves should be IS_SELF");
            }
            if (friend.getUsername().equals(username2)) {
                containsUser2 = true;
                assertEquals(FriendshipStatus.CONFIRMED, friend.getStatus(), "User1 and User2 should be CONFIRMED friends");
            }
            if (friend.getUsername().equals(username3)) {
                containsUser3 = true;
                assertEquals(FriendshipStatus.NOTADDED, friend.getStatus(), "User1 and User3 should be NOTADDED friends");
            }
        }

        assertTrue(containsUser1, "Search results should contain user1");
        assertTrue(containsUser2, "Search results should contain user2");
        assertTrue(containsUser3, "Search results should contain user3");
    }

    @Test
    public void getUserByUsername_UnusedUsername() {
        ResponseEntity<Set<FriendshipDTO>> unusedUsernameResponse = friendController.getUserByUsername(user1Header, unusedUsername);
        assertEquals(HttpStatus.OK, unusedUsernameResponse.getStatusCode(), "Valid user searching an unused username should return 'OK' status");
        assertNotNull(unusedUsernameResponse.getBody(), "Searching for an unused username should not return null body");
        assertTrue(unusedUsernameResponse.getBody().isEmpty(), "Searching for an unused username should return an empty Set");
    }

    @Test
    public void getUserByUsername_InvalidHeader() {
        ResponseEntity<Set<FriendshipDTO>> invalidHeaderResponse = friendController.getUserByUsername(invalidUserAuthHeader, username1);
        assertEquals(HttpStatus.UNAUTHORIZED, invalidHeaderResponse.getStatusCode(), "An invalid header searching for valid user should return 'UNAUTHORIZED' status");
        assertNotNull(invalidHeaderResponse.getBody(), "Searching with an invalid header should not return null body");
        assertTrue(invalidHeaderResponse.getBody().isEmpty(), "Searching with an invalid header should return an empty Set");
    }

    @Test
    public void getUserByUsername_InvalidUsername() {
        ResponseEntity<Set<FriendshipDTO>> invalidUsernameResponse = friendController.getUserByUsername(username1, invalidUsername);
        assertEquals(HttpStatus.UNAUTHORIZED, invalidUsernameResponse.getStatusCode(), "A valid user searching for an invalid username should return 'BAD_REQUEST' status");
        assertNotNull(invalidUsernameResponse.getBody(), "Searching for an invalid username should not return null body");
        assertTrue(invalidUsernameResponse.getBody().isEmpty(), "Searching for an invalid username should return an empty Set");
    }

    @Test
    public void getUserByUsername_IncorrectUsername() {
        ResponseEntity<Set<FriendshipDTO>> invalidParamResponse = friendController.getUserByUsername(invalidUserAuthHeader, invalidUsername);
        assertEquals(HttpStatus.UNAUTHORIZED, invalidParamResponse.getStatusCode(), "Searching with invalid parameters should return 'UNAUTHORIZED' status");
        assertNotNull(invalidParamResponse.getBody(), "Searching with invalid parameters should not return null body");
        assertTrue(invalidParamResponse.getBody().isEmpty(), "Searching for an invalid parameters should return an empty Set");
    }

    @Test
    public void getUserByUsername_NullHeader() {
        ResponseEntity<Set<FriendshipDTO>> nullHeader = friendController.getUserByUsername(null, username1);
        assertEquals(HttpStatus.UNAUTHORIZED, nullHeader.getStatusCode(), "A 'null' header should return 'UNAUTHORIZED' status");
        assertNotNull(nullHeader.getBody(), "Searching with a 'null' header should not return null body");
        assertTrue(nullHeader.getBody().isEmpty(), "Searching with a 'null' header should return an empty Set");
    }

    @Test
    public void getUserByUsername_NullUsername() {
        ResponseEntity<Set<FriendshipDTO>> nullUsernameResponse = friendController.getUserByUsername(user1Header, null);
        assertEquals(HttpStatus.BAD_REQUEST, nullUsernameResponse.getStatusCode(), "Searching for 'null' username should return 'UNAUTHORIZED' status");
        assertNotNull(nullUsernameResponse.getBody(), "Searching for a 'null' username should not return null body");
        assertTrue(nullUsernameResponse.getBody().isEmpty(), "Searching for a 'null' username should return an empty Set");
    }

    @Test
    public void getUserByUsername_NullParam() {
        ResponseEntity<Set<FriendshipDTO>> nullParamResponse = friendController.getUserByUsername(null, null);
        assertEquals(HttpStatus.UNAUTHORIZED, nullParamResponse.getStatusCode(), "Searching with 'null' parameters should return 'UNAUTHORIZED' status");
        assertNotNull(nullParamResponse.getBody(), "Searching with 'null' parameters should not return null body");
        assertTrue(nullParamResponse.getBody().isEmpty(), "Searching with 'null' parameters should return an empty Set");
    }

    @Test
    public void getUserByUsername_EmptyParam() {
        ResponseEntity<Set<FriendshipDTO>> nullParamResponse = friendController.getUserByUsername("", "");
        assertEquals(HttpStatus.UNAUTHORIZED, nullParamResponse.getStatusCode(), "Searching with 'null' parameters should return 'UNAUTHORIZED' status");
        assertNotNull(nullParamResponse.getBody(), "Searching with 'null' parameters should not return null body");
        assertTrue(nullParamResponse.getBody().isEmpty(), "Searching with 'null' parameters should return an empty Set");

    }

    @Test
    public void getFriends_Success() {
        ResponseEntity<Set<FriendshipDTO>> successResponse = friendController.getFriends(user1Header);
        assertEquals(ResponseEntity.ok().build().getStatusCode(), successResponse.getStatusCode(), "User1 getFriends should return 'OK' response");
        assertNotNull(successResponse.getBody(), "User1 getFriends should a hashset, never be null body");
        assertEquals(1, successResponse.getBody().size(), "User1 getFriends should return 1 friend");
        for (FriendshipDTO friend : successResponse.getBody()) {
            assertEquals(friend.getUsername(), username2, "User1's only friend should be User2");
            assertEquals(friend.getStatus(), FriendshipStatus.CONFIRMED, "User1 and User2 should be CONFIRMED friends");
        }
    }

    @Test
    public void getFriends_InvalidUserHeader() {
        ResponseEntity<Set<FriendshipDTO>> invalidUserHeaderResponse = friendController.getFriends(invalidUserAuthHeader);
        assertEquals(HttpStatus.UNAUTHORIZED, invalidUserHeaderResponse.getStatusCode(), "Searching with invalid header should return 'UNAUTHORIZED' status");
        assertNotNull(invalidUserHeaderResponse.getBody(), "Searching with invalid header should not return null body");
        assertTrue(invalidUserHeaderResponse.getBody().isEmpty(), "Searching with invalid header should return an empty Set");
    }

    @Test
    public void getFriends_InvalidHeader() {
        ResponseEntity<Set<FriendshipDTO>> invalidHeaderResponse = friendController.getFriends(invalidAuthHeader);
        assertEquals(HttpStatus.UNAUTHORIZED, invalidHeaderResponse.getStatusCode(), "Searching with invalid header should return 'UNAUTHORIZED' status");
        assertNotNull(invalidHeaderResponse.getBody(), "Searching with invalid header should not return null body");
        assertTrue(invalidHeaderResponse.getBody().isEmpty(), "Searching with invalid header should return an empty Set");
    }

    @Test
    public void getFriends_IncorrectHeader() {
        ResponseEntity<Set<FriendshipDTO>> incorrectHeaderResponse = friendController.getFriends(incorrectHeader);
        assertEquals(HttpStatus.UNAUTHORIZED, incorrectHeaderResponse.getStatusCode(), "Searching with invalid header should return 'UNAUTHORIZED' status");
        assertNotNull(incorrectHeaderResponse.getBody(), "Searching with invalid header should not return null body");
        assertTrue(incorrectHeaderResponse.getBody().isEmpty(), "Searching with invalid header should return an empty Set");
    }

    @Test
    public void getFriends_EmptyHeader() {
        ResponseEntity<Set<FriendshipDTO>> emptyHeaderResponse = friendController.getFriends("");
        assertEquals(HttpStatus.UNAUTHORIZED, emptyHeaderResponse.getStatusCode(), "Searching with invalid header should return 'UNAUTHORIZED' status");
        assertNotNull(emptyHeaderResponse.getBody(), "Searching with invalid header should not return null body");
        assertTrue(emptyHeaderResponse.getBody().isEmpty(), "Searching with invalid header should return an empty Set");
    }

    @Test
    public void getFriends_NullHeader() {
        ResponseEntity<Set<FriendshipDTO>> nullHeaderResponse = friendController.getFriends(null);
        assertEquals(HttpStatus.UNAUTHORIZED, nullHeaderResponse.getStatusCode(), "Searching with invalid header should return 'UNAUTHORIZED' status");
        assertNotNull(nullHeaderResponse.getBody(), "Searching with invalid header should not return null body");
        assertTrue(nullHeaderResponse.getBody().isEmpty(), "Searching with invalid header should return an empty Set");
    }

    @Test
    public void getUserByUsername_Success() {
        ResponseEntity<Set<FriendshipDTO>> response = friendController.getUserByUsername(user1Header, username2);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Valid user header should return 'OK' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(1, response.getBody().size(), "The search results should only have 1 user in it");
        for (FriendshipDTO friend : response.getBody()) {
            assertEquals(username2, friend.getUsername(), "User2 should be the only search result");
            assertEquals(FriendshipStatus.CONFIRMED, friend.getStatus(), "User1 and User2 should be CONFIRMED friends");
        }
    }

    @Test
    public void getUserByUsername_InvalidUser() {
        ResponseEntity<Set<FriendshipDTO>> response = friendController.getUserByUsername(user1Header, invalidUsername);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Invalid friend username should return 'BAD_REQUEST' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Response body should be empty for invalid username");
    }

    @Test
    public void getUserByUsername_Unauthorized() {
        ResponseEntity<Set<FriendshipDTO>> response = friendController.getUserByUsername(invalidAuthHeader, username2);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Invalid JWT token or authHeader should return 'UNAUTHORIZED' status");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Response body should be empty for unauthorized access");
    }

    @Test
    public void getFriends_Unauthorized() {
        ResponseEntity<Set<FriendshipDTO>> response = friendController.getFriends(invalidAuthHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Invalid JWT token or authHeader should return 'UNAUTHORIZED' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Response body should be empty for unauthorized access");
    }

    @Test
    public void getFriendsOf_Success() {
        ResponseEntity<Set<FriendshipDTO>> response = friendController.getFriendsOf(user1Header, userService.findUser(null, username2).getId());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Valid user header should return 'OK' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody().isEmpty(), "There should be friends returned for the given userId");
    }

    @Test
    public void getFriendsOf_InvalidUserId() {
        ResponseEntity<Set<FriendshipDTO>> response = friendController.getFriendsOf(user1Header, -200); // Arbitrary non-existent ID

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Non-existent userId should return 'NOT_FOUND' status");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Response body should be empty for non-existent userId");
    }

    @Test
    public void getFriendRequests_Success() {
        friendController.addFriend(username1, user3Header);
        ResponseEntity<Set<FriendshipDTO>> response = friendController.getFriendRequests(user1Header);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Valid user header should return 'OK' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody().isEmpty(), "There should be incoming friend requests");
        assertEquals(1, response.getBody().size(), "There should be 1 incoming friend request");
        for (FriendshipDTO friend : response.getBody()) {
            assertEquals(username3, friend.getUsername(), "User3 should be the only friend request");
            assertEquals(FriendshipStatus.PENDING, friend.getStatus(), "User3's request to User1 should be PENDING");
        }
    }

    @Test
    public void getFriendRequests_Unauthorized() {
        ResponseEntity<Set<FriendshipDTO>> response = friendController.getFriendRequests(invalidAuthHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Invalid JWT token or authHeader should return 'UNAUTHORIZED' status");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Response body should be empty for unauthorized access");
    }

    @Test
    public void addFriend_Success() {
        // remove first to reset the friendship
        friendController.removeFriend(username3, user1Header);
        ResponseEntity<Boolean> response = friendController.addFriend(username3, user1Header);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Valid user header should return 'OK' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody(), "Adding friend should be successful");
    }

    @Test
    public void addFriend_AlreadyFriends() {
        // user1 and user2 are already friends
        ResponseEntity<Boolean> response = friendController.addFriend(username2, user1Header);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Adding already friends should return 'OK' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody(), "Adding an already confirmed friend should return false");
    }

    @Test
    public void addFriend_InvalidUsername() {
        ResponseEntity<Boolean> response = friendController.addFriend(invalidUsername, user1Header);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Invalid friend username should return 'UNAUTHORIZED' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody(), "Adding an invalid friend should return false");
    }

    @Test
    public void confirmFriend_Success() {
        // First, add a friend request to test confirmation
        friendController.addFriend(username1, user3Header);

        ResponseEntity<Boolean> response = friendController.confirmFriend(username3, user1Header);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Confirming friend request should return 'OK' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody(), "Confirming the friend request should be successful");
    }

    @Test
    public void confirmFriend_Unauthorized() {
        ResponseEntity<Boolean> response = friendController.confirmFriend(username2, invalidAuthHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Invalid JWT token or authHeader should return 'UNAUTHORIZED' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody(), "Unauthorized confirmation attempt should return false");
    }

    @Test
    public void removeFriend_Success() {
        ResponseEntity<Boolean> response = friendController.removeFriend(username2, user1Header);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Valid user header should return 'OK' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody(), "Removing friend should be successful");
    }

    @Test
    public void removeFriend_FriendNotFound() {
        ResponseEntity<Boolean> response = friendController.removeFriend(user1Header, "nonexistentUsername");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Removing a non-existent friend should return 'UNAUTHORIZED' response");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody(), "Attempting to remove a non-existent friend should return false");
    }

}
