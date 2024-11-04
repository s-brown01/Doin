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

@SpringBootTest
@Transactional
public class FriendServiceTest {
    private static final String username1 = "User1_Username";
    private static final String username2 = "User2_Username";
    private static final String username3 = "User3_Username";
    private static final String securityQuestion = "pet";
    final String invalidUsername = username1 + "FAKE";

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityQuestionService securityQuestionService;


    @BeforeEach
    public void loadTables() {
        loadSecurityQuestions();
        createNewUser(username1);
        createNewUser(username2);
        createNewUser(username3);
    }

    private void loadSecurityQuestions() {
        securityQuestionService.addSecurityQuestion(securityQuestion);
    }

    private void createNewUser(String username) {
        RegisterDTO data = new RegisterDTO(username, "password", securityQuestion, "answer");
        userService.createNewUser(data);
    }

    @Test
    public void getFriendsOfFriendsTest_Success() {
        // add the friendships into the repository (assuming they work)
        // user1 -> user2
        friendService.addFriend(username1, username2);
        friendService.confirmFriend(username2, username1);
        // user2 -> user3
        friendService.addFriend(username2, username3);
        friendService.confirmFriend(username3, username2);

        // get the friends of friends for user 1 - should be User 3
        Set<FriendshipDTO> fofUser1 = friendService.getFriendsOfFriends(username1);
        Set<FriendshipDTO> fofUser3 = friendService.getFriendsOfFriends(username3);

        assertEquals(1, fofUser1.size(), "User1 should only have 1 friend of friend");
        assertEquals(1, fofUser3.size(), "User3 should only have 1 friend of friend");

        boolean fofUser1ContainsUser3 = false;
        // make sure that the fof of user1 contains user3
        for (FriendshipDTO friendship : fofUser1) {
            // if the username is in there, it is good enough
            if (friendship.getUsername().equals(username3)) {
                fofUser1ContainsUser3 = true;
                break;
            }
        }
        boolean fofUser3ContainsUser1 = false;
        // make sure that the fof of user3 contains user1
        for (FriendshipDTO friendship : fofUser3) {
            System.out.println("FOF3 " + friendship);
            // if the username is in there, it is good enough
            if (friendship.getUsername().equals(username1)) {
                fofUser3ContainsUser1 = true;
                break;
            }
        }
        assertTrue(fofUser1ContainsUser3, "User3 should be in User1's friends of friends");
        assertTrue(fofUser3ContainsUser1, "User1 should be in User3's friends of friends");
    }

    @Test
    public void getFriendsOfFriendsTest_Invalid() {
        Set<FriendshipDTO> invalidSearchResults = friendService.getFriendsOfFriends(invalidUsername);
        assertEquals(0, invalidSearchResults.size(), "Invalid username should have no friends of friends");

        Set<FriendshipDTO> validSearchResults = friendService.getFriendsOfFriends(username1);
        assertEquals(0, validSearchResults.size(), "User1 should have no friends of friends");
    }

    @Test
    public void getFriendsOfFriendsTest_Null(){
        Set<FriendshipDTO> invalidSearchResults = friendService.getFriendsOfFriends(null);
        assertEquals(0, invalidSearchResults.size(), "'null' should have no friends of friends");
    }

    @Test
    public void getUserTest_Success() {
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
    public void getUserTest_Invalid() {
        Set<FriendshipDTO> searchResults = friendService.getUser(username1, invalidUsername);
        assertEquals(0, searchResults.size(), "User1 searching for invalid should return no users.");
        searchResults = friendService.getUser(invalidUsername, username1);
        assertEquals(0, searchResults.size(), "An invalid user searching for User1 should return no users.");
    }

    @Test
    public void getUserTest_Null() {
        Set<FriendshipDTO> searchResults = friendService.getUser(username1, null);
        assertEquals(0, searchResults.size(), "User1 searching for 'null' should return no users.");
        searchResults = friendService.getUser(null, null);
        assertEquals(0, searchResults.size(), "'null' searching for 'null' should return no users.");
        searchResults = friendService.getUser(null, username1);
        assertEquals(0, searchResults.size(), "'null' searching for User1 should return no users.");
    }

    @Test
    public void getFriends_Success() {
        // user1 <-> user2
        friendService.addFriend(username1, username2);
        friendService.confirmFriend(username2, username1);
        // user3 <-> user2
        friendService.addFriend(username2, username3);
        friendService.confirmFriend(username3, username2);

        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);

        assertEquals(1, user1Friends.size(), "User1 should have 1 friend");
        assertEquals(2, user2Friends.size(), "User2 should have 2 friend");
        assertEquals(1, user3Friends.size(), "User3 should have 1 friend");

        boolean user1ContainsUser2 = false;
        boolean user1ContainsUser3 = false;
        for (FriendshipDTO friend : user1Friends) {
            if (friend.getUsername().equals(username2)) {
                user1ContainsUser2 = true;
            }
            if (friend.getUsername().equals(username3)) {
                user1ContainsUser3 = true;
            }
        }
        // 1 -> 2 = true
        // 1 -> 3 = false
        assertTrue(user1ContainsUser2, "User1 should be friends with User2");
        assertFalse(user1ContainsUser3, "User1 should not be friends with User3");

        boolean user2ContainsUser1 = false;
        boolean user2ContainsUser3 = false;
        for (FriendshipDTO friend : user2Friends) {
            if (friend.getUsername().equals(username1)) {
                user2ContainsUser1 = true;
            }
            if (friend.getUsername().equals(username3)) {
                user2ContainsUser3 = true;
            }
        }
        // 2 -> 1 = true
        // 2 -> 3 = true
        assertTrue(user2ContainsUser1, "User2 should be friends with User1");
        assertTrue(user2ContainsUser3, "User2 should be friends with User3");

        boolean user3ContainsUser1 = false;
        boolean user3ContainsUser2 = false;
        for (FriendshipDTO friend : user3Friends) {
            if (friend.getUsername().equals(username1)) {
                user3ContainsUser1 = true;
            }
            if (friend.getUsername().equals(username2)) {
                user3ContainsUser2 = true;
            }
        }
        // 3 -> 1 = false
        // 3 -> 2 = true
        assertFalse(user3ContainsUser1, "User3 should not be friends with User1");
        assertTrue(user3ContainsUser2, "User3 should be friends with User2");

        // removing friends
        // now user1 and user2 no longer friends
        friendService.removeFriend(username1, username2);

        user1Friends = friendService.getFriends(username1);
        user2Friends = friendService.getFriends(username2);
        assertTrue(user1Friends.isEmpty(), "User1 should now have 0 friends");
        assertEquals(1, user2Friends.size(), "User2 should now have 1 friend");


        user1ContainsUser2 = false; // reset this variable
        // user1ContainsUser3 is already asserted 'false'
        for (FriendshipDTO friend : user1Friends) {
            if (friend.getUsername().equals(username2)) {
                user1ContainsUser2 = true;
            }
            if (friend.getUsername().equals(username3)) {
                user1ContainsUser3 = true;
            }
        }
        // 1 -> 2 = false
        // 1 -> 3 = false
        assertFalse(user1ContainsUser2, "User1 should not be friends with User2");
        assertFalse(user1ContainsUser3, "User1 should not be friends with User3");

        user2ContainsUser1 = false;
        user2ContainsUser3 = false;
        for (FriendshipDTO friend : user2Friends) {
            if (friend.getUsername().equals(username1)) {
                user2ContainsUser1 = true;
            }
            if (friend.getUsername().equals(username3)) {
                user2ContainsUser3 = true;
            }
        }
        // 2 -> 1 = false
        // 2 -> 3 = true
        assertFalse(user2ContainsUser1, "User2 should not be friends with User1");
        assertTrue(user2ContainsUser3, "User2 should be friends with User3");
    }

    @Test
    public void getFriends_Invalid() {
        // should be no friendships
        final Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        final Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        final Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);
        final Set<FriendshipDTO> invalidFriends = friendService.getFriends(invalidUsername);

        assertTrue(user1Friends.isEmpty(), "User1 should have no friends");
        assertTrue(user2Friends.isEmpty(), "User2 should have no friends");
        assertTrue(user3Friends.isEmpty(), "User3 should have no friends");
        assertTrue(invalidFriends.isEmpty(), "Invalid username should have no friends");
    }

    @Test
    public void getFriends_Null(){
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
        friendService.addFriend(invalidUsername, username1);
        friendService.addFriend(username1, invalidUsername);
        Set<FriendshipDTO> user1Requests = friendService.getFriendRequests(username1);
        Set<FriendshipDTO> invalidRequests = friendService.getFriendRequests(invalidUsername);
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
        assertFalse(friendService.addFriend(username1, invalidUsername).isValid(), "User1 should not be able to friend a user who doesn't exist");

        // User1 tries to friend themselves
        assertFalse(friendService.addFriend(username1, username1).isValid(), "User1 should not be able to friend themselves");

        // Verify friendships remain unchanged after invalid attempts
        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);

        assertEquals(1, user1Friends.size(), "User1 should still have 2 friends");
        assertEquals(1, user2Friends.size(), "User2 should still have 1 friend");
        assertEquals(0, user3Friends.size(), "User3 should still have 1 friend");
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
        assertFalse(friendService.removeFriend(username1, invalidUsername).isValid(), "User1 should not be able to unfriend a user who doesn't exist");
        assertFalse(friendService.removeFriend(username1, username1).isValid(), "User1 should not be able to unfriend themselves");
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
