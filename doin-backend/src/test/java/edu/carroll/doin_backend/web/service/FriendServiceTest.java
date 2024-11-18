package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for FriendService functionality in the application.
 * <p>
 * This suite covers various aspects of friend-related operations such as:
 * <ul>
 *     <li>Getting friends of friends</li>
 *     <li>Searching for a specific user</li>
 *     <li>Getting the immediate friends</li>
 *     <li>Getting incoming friend requests </li>
 *     <li>Adding friends</li>
 *     <li>Removing friends</li>
 *     <li>Confirming friends</li>
 * </ul>
 * </p>
 * Each test method is transactional, ensuring that no changes persist
 * in the database after each test, which keeps tests isolated.
 */
@SpringBootTest
@Transactional
public class FriendServiceTest {
    /**
     * The unique username of a user, AKA User1
     */
    private static final String username1 = "User1_Username";
    /**
     * The unique username of a user, AKA User2
     */
    private static final String username2 = "User2_Username";
    /**
     * The unique username of a user, AKA User3
     */
    private static final String username3 = "User3_Username";
    /**
     * The unique username of a user AKA user4
     */
    private static final String username4 = "User4_Username";
    /**
     * The unique username of a user AKA user5
     */
    private static final String username5 = "User5_Username";
    /**
     * A username of a user that is NOT created/in the database
     */
    final String unusedUsername = username1 + "FAKE";

    /**
     * The FriendService to be tested
     */
    @Autowired
    private FriendService friendService;

    /**
     * A UserService to create new Users
     */
    @Autowired
    private UserService userService;
    /**
     * A SecurityQuestionService since creating newUsers require a valid SecurityQuestion
     */
    @Autowired
    private SecurityQuestionService securityQuestionService;

    /**
     * Sets up required data tables for testing by adding security questions and registering test users.
     */
    @BeforeEach
    public void loadTables() {
        final String securityQuestion = "temp security question";
        // add a security question to the table
        securityQuestionService.addSecurityQuestion(securityQuestion);
        createNewUser(username1, securityQuestion);
        createNewUser(username2, securityQuestion);
        createNewUser(username3, securityQuestion);
        createNewUser(username4, securityQuestion);
        createNewUser(username5, securityQuestion);
    }

    private void createNewUser(String username, String securityQuestion) {
        RegisterDTO data = new RegisterDTO(username, "password", securityQuestion, "answer");
        userService.createNewUser(data);
    }

    @Test
    public void getFriendsOfFriends_OneMutual() {
        // add the friendships into the repository (assuming they work)
        // user1 -> user2
        friendService.addFriend(username1, username2);
        friendService.confirmFriend(username2, username1);
        // user2 -> user3
        friendService.addFriend(username2, username3);
        friendService.confirmFriend(username3, username2);

        // get the friends of friends for user 1 - should be User 3
        Set<FriendshipDTO> fofUser1 = friendService.getFriendsOfFriends(username1);

        assertEquals(1, fofUser1.size(), "User1 should only have 1 friend of friend");

        boolean fofUser1ContainsUser3 = false;
        // make sure that the fof of user1 contains user3
        for (FriendshipDTO friendship : fofUser1) {
            // if the username is in there, it is good enough
            if (friendship.getUsername().equals(username3)) {
                fofUser1ContainsUser3 = true;
                break;
            }
        }
        assertTrue(fofUser1ContainsUser3, "User3 should be in User1's friends of friends");
    }

    @Test
    public void getFriendsOfFriends_TwoMutualFromOneFriend() {
        // user1 -> user2
        assertTrue(friendService.addFriend(username1, username2).isValid(), "Making sure addFriend works: User1 - User2");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "Making sure addFriend works: User2 - User1");
        // user2 -> user3
        assertTrue(friendService.addFriend(username2, username3).isValid(), "Making sure addFriend works: User2 - User3");
        assertTrue(friendService.confirmFriend(username3, username2).isValid(), "Making sure addFriend works: User3 - User2");
        // user2 -> user4
        assertTrue(friendService.addFriend(username2, username4).isValid(), "Making sure addFriend works: User2 - User4");
        assertTrue(friendService.confirmFriend(username4, username2).isValid(), "Making sure addFriend works: User4 - User2");

        // get the friends of friends for user 1 - should be User 3
        Set<FriendshipDTO> fofUser1 = friendService.getFriendsOfFriends(username1);

        assertEquals(2, fofUser1.size(), "User1 should have 2 friend of friends");

        boolean fofUser1ContainsUser3 = false;
        boolean fofUser1ContainsUser4 = false;
        // make sure that the fof of user1 contains user3
        // make sure that the fof of user1 contains user4
        for (FriendshipDTO friendship : fofUser1) {
            // check user3
            if (friendship.getUsername().equals(username3)) {
                fofUser1ContainsUser3 = true;
            }
            // check user4
            if (friendship.getUsername().equals(username4)) {
                fofUser1ContainsUser4 = true;
            }
            // if they both are there, end it
            if (fofUser1ContainsUser3 && fofUser1ContainsUser4) {
                break;
            }
        }
        assertTrue(fofUser1ContainsUser3, "User3 should be in User1's friends of friends");
        assertTrue(fofUser1ContainsUser4, "User4 should be in User1's friends of friends");
    }

    @Test
    public void getFriendsOfFriends_OneMutualFromTwoFriends() {
        // user1 -> user2
        assertTrue(friendService.addFriend(username1, username2).isValid(), "Making sure addFriend works: User1 - User2");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "Making sure addFriend works: User2 - User1");
        // user2 -> user3
        assertTrue(friendService.addFriend(username2, username3).isValid(), "Making sure addFriend works: User2 - User3");
        assertTrue(friendService.confirmFriend(username3, username2).isValid(), "Making sure addFriend works: User3 - User2");
        // user1 -> user4
        assertTrue(friendService.addFriend(username1, username4).isValid(), "Making sure addFriend works: User1 - User4");
        assertTrue(friendService.confirmFriend(username4, username1).isValid(), "Making sure addFriend works: User4 - User1");
        // user4 -> user5
        assertTrue(friendService.addFriend(username4, username5).isValid(), "Making sure addFriend works: User4 - User5");
        assertTrue(friendService.confirmFriend(username5, username4).isValid(), "Making sure addFriend works: User5 - User4");

        // user1 -> user2 -> user3
        // user1 -> user4 -> user5
        Set<FriendshipDTO> fofUser1 = friendService.getFriendsOfFriends(username1);

        assertEquals(2, fofUser1.size(), "User1 should have 2 friends of friends");
        boolean fofUser1ContainsUser3 = false;
        boolean fofUser1ContainsUser5 = false;
        for (FriendshipDTO friendship : fofUser1) {
            if (friendship.getUsername().equals(username3)) {
                fofUser1ContainsUser3 = true;
            }
            if (friendship.getUsername().equals(username5)) {
                fofUser1ContainsUser5 = true;
            }
            if (fofUser1ContainsUser3 && fofUser1ContainsUser5) {
                break;
            }
        }

        assertTrue(fofUser1ContainsUser3, "User3 should be in User1's friends of friends");
        assertTrue(fofUser1ContainsUser5, "User5 should be in User1's friends of friends");
    }

    @Test
    public void getFriendsOfFriends_NoFriends() {
        Set<FriendshipDTO> validSearchResults = friendService.getFriendsOfFriends(username1);
        assertEquals(0, validSearchResults.size(), "User1 should have no friends of friends");
    }

    @Test
    public void getFriendsOfFriends_UnusedUsername() {
        Set<FriendshipDTO> invalidSearchResults = friendService.getFriendsOfFriends(unusedUsername);
        assertTrue(invalidSearchResults.isEmpty(), "Invalid username should have no friends of friends");
    }

    @Test
    public void getFriendsOfFriends_Null() {
        Set<FriendshipDTO> invalidSearchResults = friendService.getFriendsOfFriends(null);
        assertEquals(0, invalidSearchResults.size(), "'null' should have no friends of friends");
    }

    @Test
    public void getUserTest_OneResult() {
        Set<FriendshipDTO> searchResults = friendService.getUser(username1, username2);
        assertEquals(1, searchResults.size(), "The returned set should only have 1 User in it: User2");

        FriendshipStatus user2Status = null;
        boolean containsUser2 = false;
        for (FriendshipDTO friend : searchResults) {
            if (friend.getUsername().equals(username2)) {
                user2Status = friend.getStatus();
                containsUser2 = true;
            }
        }

        assertTrue(containsUser2, "User1 should be able to search for and find User2");
        assertEquals(FriendshipStatus.NOTADDED, user2Status, "User1 and User1 should not be added");
    }

    @Test
    public void getUserTest_ManyResults() {
        // user2 and user1 should be confirmed friends
        assertTrue(friendService.addFriend(username1, username2).isValid(), "Adding user1 to user2");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "Confirming user2 to user1");
        // user1 added user3
        assertTrue(friendService.addFriend(username1, username3).isValid(), "Adding user1 to user3");
        // user4 added user1
        assertTrue(friendService.addFriend(username4, username1).isValid(), "Adding user4 to user1");

        Set<FriendshipDTO> searchResults = friendService.getUser(username1, "user");
        assertEquals(5, searchResults.size(), "Searching for user 'user' should return 4 users");
        boolean containsUser1 = false;
        boolean containsUser2 = false;
        boolean containsUser3 = false;
        boolean containsUser4 = false;
        boolean containsUser5 = false;
        for (FriendshipDTO friend : searchResults) {
            switch (friend.getUsername()) {
                case username1 -> {
                    assertEquals(friend.getStatus(), FriendshipStatus.IS_SELF, "User1 status with themselves should be IS_SELF");
                    containsUser1 = true;
                }
                case username2 -> {
                    assertEquals(friend.getStatus(), FriendshipStatus.CONFIRMED, "User1 and User2 should be CONFIRMED friends");
                    containsUser2 = true;
                }
                case username3 -> {
                    assertEquals(friend.getStatus(), FriendshipStatus.PENDING, "User1 and User3 should be PENDING friends");
                    containsUser3 = true;
                }
                case username4 -> {
                    assertEquals(friend.getStatus(), FriendshipStatus.PENDING, "User1 and User4 should be PENDING friends");
                    containsUser4 = true;
                }
                case username5 -> {
                    assertEquals(friend.getStatus(), FriendshipStatus.NOTADDED, "User1 and User5 should be NOTADDED friends");
                    containsUser5 = true;
                }
            }
        }

        assertTrue(containsUser1, "User1 should be able to search for and find User1");
        assertTrue(containsUser2, "User1 should be able to search for and find User2");
        assertTrue(containsUser3, "User1 should be able to search for and find User3");
        assertTrue(containsUser4, "User1 should be able to search for and find User4");
        assertTrue(containsUser5, "User1 should be able to search for and find User5");
    }

    @Test
    public void getUserTest_SearchedByUnusedUsername() {
        Set<FriendshipDTO> searchResults = friendService.getUser(unusedUsername, username1);
        assertEquals(0, searchResults.size(), "An invalid user searching for User1 should return no users.");
    }

    @Test
    public void getUserTest_SearchForUnusedUsername() {
        Set<FriendshipDTO> searchResults = friendService.getUser(username1, unusedUsername);
        assertEquals(0, searchResults.size(), "User1 searching for invalid should return no users.");
    }

    @Test
    public void getUserTest_Nulls() {
        Set<FriendshipDTO> searchResults = friendService.getUser(username1, null);
        assertEquals(0, searchResults.size(), "User1 searching for 'null' should return no users.");
        searchResults = friendService.getUser(null, null);
        assertEquals(0, searchResults.size(), "'null' searching for 'null' should return no users.");
        searchResults = friendService.getUser(null, username1);
        assertEquals(0, searchResults.size(), "'null' searching for User1 should return no users.");
    }

    @Test
    public void getFriends_OneFriend() {
        // user1 -> user2
        assertTrue(friendService.addFriend(username1, username2).isValid(), "Making sure addFriend works: User1 - User2");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "Making sure addFriend works: User2 - User1");

        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);

        assertEquals(1, user1Friends.size(), "User1 should have 1 friend");
        assertEquals(1, user2Friends.size(), "User2 should have 1 friend");
        // user1 friend's
        boolean user1ContainsUser2 = false;
        for (FriendshipDTO friend : user1Friends) {
            if (friend.getUsername().equals(username2)) {
                user1ContainsUser2 = true;
                assertEquals(friend.getStatus(), FriendshipStatus.CONFIRMED, "User1 and user2 should be CONFIRMED friends");
                break;
            }
        }
        assertTrue(user1ContainsUser2, "User1 should be friends with User2");
        // user2 friends
        boolean user2ContainsUser1 = false;
        for (FriendshipDTO friend : user2Friends) {
            if (friend.getUsername().equals(username1)) {
                user2ContainsUser1 = true;
                assertEquals(friend.getStatus(), FriendshipStatus.CONFIRMED, "User1 and user2 should be CONFIRMED friends");
                break;
            }
        }
        assertTrue(user2ContainsUser1, "User2 should be friends with User1");
    }

    @Test
    public void getFriends_MultipleFriends() {
        // user1 -> user2
        assertTrue(friendService.addFriend(username1, username2).isValid(), "Making sure addFriend works: User1 - User2");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "Making sure addFriend works: User2 - User1");
        // user1 -> user3
        assertTrue(friendService.addFriend(username1, username3).isValid(), "Making sure addFriend works: User1 - User4");
        assertTrue(friendService.confirmFriend(username3, username1).isValid(), "Making sure addFriend works: User4 - User1");

        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);

        assertEquals(2, user1Friends.size(), "User1 should have 2 friends");
        assertEquals(1, user2Friends.size(), "User2 should have 1 friends");
        assertEquals(1, user3Friends.size(), "User3 should have 1 friends");
        // user1's friends
        boolean user1ContainsUser2 = false;
        boolean user1ContainsUser3 = false;
        for (FriendshipDTO friend : user1Friends) {
            if (friend.getUsername().equals(username2)) {
                assertEquals(friend.getStatus(), FriendshipStatus.CONFIRMED, "User1 and user2 should be CONFIRMED friends");
                user1ContainsUser2 = true;
                continue;
            }
            if (friend.getUsername().equals(username3)) {
                assertEquals(friend.getStatus(), FriendshipStatus.CONFIRMED, "User1 and user3 should be CONFIRMED friends");
                user1ContainsUser3 = true;
                continue;
            }
            if (user1ContainsUser2 && user1ContainsUser3) {
                break;
            }
        }
        assertTrue(user1ContainsUser2, "User1 should be friends with User2");
        assertTrue(user1ContainsUser3, "User1 should be friends with User3");

        boolean user2ContainsUser1 = false;
        for (FriendshipDTO friend : user2Friends) {
            if (friend.getUsername().equals(username1)) {
                assertEquals(friend.getStatus(), FriendshipStatus.CONFIRMED, "User2 and User1 should be CONFIRMED friends");
                user2ContainsUser1 = true;
                break;
            }
        }
        assertTrue(user2ContainsUser1, "User2 should be friends with User1");

        boolean user3ContainsUser1 = false;
        for (FriendshipDTO friend : user3Friends) {
            if (friend.getUsername().equals(username1)) {
                assertEquals(friend.getStatus(), FriendshipStatus.CONFIRMED, "User3 and User1 should be CONFIRMED friends");
                user3ContainsUser1 = true;
                break;
            }
        }
        assertTrue(user3ContainsUser1, "User3 should be friends with User1");
    }

    @Test
    public void getFriends_Invalid() {
        // should be no friendships
        final Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        final Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        final Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);
        final Set<FriendshipDTO> invalidFriends = friendService.getFriends(unusedUsername);

        assertTrue(user1Friends.isEmpty(), "User1 should have no friends");
        assertTrue(user2Friends.isEmpty(), "User2 should have no friends");
        assertTrue(user3Friends.isEmpty(), "User3 should have no friends");
        assertTrue(invalidFriends.isEmpty(), "Invalid username should have no friends");
    }

    @Test
    public void getFriends_Null() {
        final Set<FriendshipDTO> nullFriends = friendService.getFriends(null);
        assertTrue(nullFriends.isEmpty(), "'Null' friend should return no friends");

        friendService.addFriend(username1, null);
        friendService.confirmFriend(null, username1);
        friendService.addFriend(username1, username1);
        friendService.confirmFriend(username1, username1);
        final Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        assertTrue(user1Friends.isEmpty(), "User1 friend should have no friends");
    }

    @Test
    public void getFriendsOf_NoFriends() {
        final Set<FriendshipDTO> user1GetUser2Friends = friendService.getFriendsOf(username1, 2);
        assertTrue(user1GetUser2Friends.isEmpty(), "User1 should be able to see that User2 has no friends");
    }

    @Test
    public void getFriendsOf_OneFriend() {
        // user1 -> user2
        assertTrue(friendService.addFriend(username1, username2).isValid(), "Making sure addFriend works: User1 and User2");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "Making sure addFriend works: User2 and User1");
        final Set<FriendshipDTO> user1GetUser2Friends = friendService.getFriendsOf(username1, userService.findUser(null, username2).getId());
        assertEquals(1, user1GetUser2Friends.size(), "User1 should be able to see that User2 has 1 friend");
        for (FriendshipDTO friend : user1GetUser2Friends) {
            if (friend.getUsername().equals(username1)) {
                assertEquals(friend.getStatus(), FriendshipStatus.IS_SELF, "User1 status with themselves should be IS_SELF");
            } else {
                fail("User2 friends should only contain user1");
            }
        }
    }

    @Test
    public void getFriendsOf_TwoFriends() {
        // user1 -> user2
        assertTrue(friendService.addFriend(username1, username2).isValid(), "Making sure addFriend works: User1 and User2");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "Making sure addFriend works: User2 and User1");
        // user2 -> user3
        assertTrue(friendService.addFriend(username2, username3).isValid(), "Making sure addFriend works: User2 and User 3");
        assertTrue(friendService.confirmFriend(username3, username2).isValid(), "Making sure addFriend works: User3 and User2");

        final Set<FriendshipDTO> user1GetUser2Friends = friendService.getFriendsOf(username1, userService.findUser(null, username2).getId());
        assertEquals(2, user1GetUser2Friends.size(), "User1 should be able to see that User2 has 2 friends");

        for (FriendshipDTO friend : user1GetUser2Friends) {
            switch (friend.getUsername()) {
                case username1 ->
                        assertEquals(friend.getStatus(), FriendshipStatus.IS_SELF, "User1 status with themselves should be IS_SELF");
                case username3 ->
                        assertEquals(friend.getStatus(), FriendshipStatus.NOTADDED, "User1 and User3 should be NOT ADDED friends");
                default -> fail("Should only contain user1 and user3");
            }
        }
    }

    @Test
    public void getFriendsOf_SearchedByUnusedUsername() {
        final Set<FriendshipDTO> searchResults = friendService.getFriendsOf(unusedUsername, userService.findUser(null, username1).getId());
        assertTrue(searchResults.isEmpty(), "An unused username should not be able to see user1's friends");
    }

    @Test
    public void getFriendsOf_BadID() {
        final Set<FriendshipDTO> searchResults = friendService.getFriendsOf(username1, -1);
        assertTrue(searchResults.isEmpty(), "Getting friends of an invalid ID should be an empty set");
    }

    @Test
    public void getFriendRequests_Success() {
        // all users add user1
        friendService.addFriend(username1, username1);
        friendService.addFriend(username2, username1);
        friendService.addFriend(username3, username1);
        // user 2 add user 3
        friendService.addFriend(username2, username3);
        // should only be 2 incoming friend requests
        Set<FriendshipDTO> user1Requests = friendService.getFriendRequests(username1);
        assertEquals(2, user1Requests.size(), "User1 should have 2 incoming friend requests");
        boolean user1ContainsUser1 = false;
        boolean user1ContainsUser2 = false;
        boolean user1ContainsUser3 = false;
        for (FriendshipDTO friend : user1Requests) {
            if (friend.getUsername().equals(username1)) {
                user1ContainsUser1 = true;
            }
            if (friend.getUsername().equals(username2)) {
                user1ContainsUser2 = true;
            }
            if (friend.getUsername().equals(username3)) {
                user1ContainsUser3 = true;
            }
        }
        assertFalse(user1ContainsUser1, "User1 should not have an incoming friend request from User1");
        assertTrue(user1ContainsUser2, "User1 should have an incoming friend request from User2");
        assertTrue(user1ContainsUser3, "User1 should have an incoming friend request from User3");

        Set<FriendshipDTO> user2Requests = friendService.getFriendRequests(username2);
        // instead of using isEmpty, using size just to match format of the rest
        assertEquals(0, user2Requests.size(), "User2 should have 0 incoming friend requests");

        Set<FriendshipDTO> user3Requests = friendService.getFriendRequests(username3);
        assertEquals(1, user3Requests.size(), "User3 should have 1 incoming friend request");
        boolean user3ContainsUser1 = false;
        boolean user3ContainsUser2 = false;
        boolean user3ContainsUser3 = false;
        for (FriendshipDTO friend : user3Requests) {
            if (friend.getUsername().equals(username1)) {
                user3ContainsUser1 = true;
            }
            if (friend.getUsername().equals(username2)) {
                user3ContainsUser2 = true;
            }
            if (friend.getUsername().equals(username3)) {
                user3ContainsUser3 = true;
            }
        }
        assertFalse(user3ContainsUser1, "User3 should not have an incoming friend request from User1");
        assertTrue(user3ContainsUser2, "User3 should have an incoming friend request from User2");
        assertFalse(user3ContainsUser3, "User3 should not have an incoming friend request from User3");
    }

    @Test
    public void getFriendRequests_Invalid() {
        friendService.addFriend(unusedUsername, username1);
        friendService.addFriend(username1, unusedUsername);
        Set<FriendshipDTO> user1Requests = friendService.getFriendRequests(username1);
        Set<FriendshipDTO> invalidRequests = friendService.getFriendRequests(unusedUsername);
        assertTrue(user1Requests.isEmpty(), "User1 should have no friend requests");
        assertTrue(invalidRequests.isEmpty(), "Invalid username should have no friend requests");
    }

    @Test
    public void getFriendRequests_Null() {
        friendService.addFriend(username1, null);
        friendService.addFriend(null, username1);
        friendService.addFriend(null, null);
        Set<FriendshipDTO> user1Requests = friendService.getFriendRequests(username1);
        Set<FriendshipDTO> nullRequests = friendService.getFriendRequests(null);
        assertTrue(user1Requests.isEmpty(), "User1 should have no friend requests");
        assertTrue(nullRequests.isEmpty(), "'null' username should have no friend requests");
    }

    @Test
    public void addFriendTest_Success() {
        // User1 adds User2 as a friend
        assertTrue(friendService.addFriend(username1, username2).isValid(), "User1 should be able to add User2 as a friend");

        // User2 confirms the friendship with User1
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "User2 should confirm the friendship with User1");

        // User1 adds User3 as a friend
        assertTrue(friendService.addFriend(username1, username3).isValid(), "User1 should be able to add User3 as a friend");

        // User3 confirms the friendship with User1
        assertTrue(friendService.confirmFriend(username3, username1).isValid(), "User3 should confirm the friendship with User1");

        // Verify the number of friends for each user
        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);

        assertEquals(2, user1Friends.size(), "User1 should have 2 friends");
        assertEquals(1, user2Friends.size(), "User2 should have 1 friend");
        assertEquals(1, user3Friends.size(), "User3 should have 1 friend");
    }

    @Test
    public void addFriendTest_Invalid() {
        assertTrue(friendService.addFriend(username1, username2).isValid(), "User1 should be able to add User2 as a friend");
        // User1 tries to friend User2 again
        assertFalse(friendService.addFriend(username1, username2).isValid(), "User1 should not be able to friend User2 twice");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "User2 should confirm the friendship with User2");
        assertFalse(friendService.confirmFriend(username2, username1).isValid(), "User2 should not be able to friend User1 twice");

        // User1 tries to friend a non-existent user
        assertFalse(friendService.addFriend(username1, unusedUsername).isValid(), "User1 should not be able to friend a user who doesn't exist");

        // User1 tries to friend themselves
        assertFalse(friendService.addFriend(username1, username1).isValid(), "User1 should not be able to friend themselves");

        // Verify friendships remain unchanged after invalid attempts
        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);

        assertEquals(1, user1Friends.size(), "User1 should have 1 friends");
        assertEquals(1, user2Friends.size(), "User2 should have 1 friend");
        assertEquals(0, user3Friends.size(), "User3 should have 1 friend");
    }

    @Test
    public void addFriendTest_Null() {
        // User1 tries to friend null
        assertFalse(friendService.addFriend(username1, null).isValid(), "User1 should not be able to friend 'null'");

        // Both users are null
        assertFalse(friendService.addFriend(null, null).isValid(), "'null' should not be able to friend 'null'");

        // User2 tries to friend null
        assertFalse(friendService.addFriend(null, username1).isValid(), "'null' should not be able to friend User1");

        // Verify friendships remain unchanged after null attempts
        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);

        assertEquals(0, user1Friends.size(), "User1 should have 0 friends");
    }

    @Test
    public void removeFriendTest_Success() {
        friendService.addFriend(username1, username2);
        friendService.confirmFriend(username2, username1);
        assertTrue(friendService.removeFriend(username1, username2).isValid(), "User1 should be able to unfriend User2");
        // bad paths
        assertFalse(friendService.removeFriend(username1, username2).isValid(), "User1 should not be able to unfriend User2 twice");
        assertFalse(friendService.removeFriend(username1, null).isValid(), "User1 should not be able to unfriend 'null'");
        assertFalse(friendService.removeFriend(null, null).isValid(), "'null' should not be able to unfriend 'null'");
        assertFalse(friendService.removeFriend(username1, unusedUsername).isValid(), "User1 should not be able to unfriend a user who doesn't exist");
        assertFalse(friendService.removeFriend(username1, username1).isValid(), "User1 should not be able to unfriend themselves");
    }

    @Test
    public void removeFriendTest_Invalid() {
        friendService.addFriend(username1, username2);
        assertTrue(friendService.removeFriend(username1, username2).isValid(), "User1 should be able to unfriend User2");
        assertFalse(friendService.removeFriend(username1, username2).isValid(), "User1 should not be able to unfriend User2 twice");

        assertFalse(friendService.removeFriend(username1, unusedUsername).isValid(), "User1 should not be able to unfriend a user who doesn't exist");
        assertFalse(friendService.removeFriend(username1, username1).isValid(), "User1 should not be able to unfriend themselves");
        assertFalse(friendService.removeFriend(unusedUsername, username1).isValid(), "'Invalid' username should not be able to unfriend User1");
        assertFalse(friendService.removeFriend(unusedUsername, unusedUsername).isValid(), "'Invalid' username should not be able to unfriend themselves");
    }

    @Test
    public void removeFriendTest_Null() {
        assertFalse(friendService.removeFriend(username1, null).isValid(), "User1 should not be able to unfriend 'null'");
        assertFalse(friendService.removeFriend(null, username1).isValid(), "'null' should not be able to unfriend User1");
        assertFalse(friendService.removeFriend(null, null).isValid(), "'null' should not be able to unfriend themselves");
    }

    @Test
    public void confirmFriendTest_Success() {
        assertTrue(friendService.confirmFriend(username1, username2).isValid(), "User1 should be able to confirm User2");
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "User1 should be able to confirm User2");
    }

    @Test
    public void confirmFriendTest_Invalid() {
        assertFalse(friendService.confirmFriend(username1, username1).isValid(), "User1 should not be able to confirm themselves");
    }

    @Test
    public void confirmFriendTest_Null() {
        assertFalse(friendService.confirmFriend(null, null).isValid(), "Null users should be able to confirm themselves");
        assertFalse(friendService.confirmFriend(username1, null).isValid(), "User1 should not be able to confirm themselves");
        assertFalse(friendService.confirmFriend(null, username1).isValid(), "Null users should be able to confirm User1");
    }
}
