package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
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
    public void getFriendsOfFriendsTest_Normal() {
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
    public void getUserTest() {
        
    }

    @Test
    public void addFriendTest() {
        // user1 -> user2
        assertTrue(friendService.addFriend(username1, username2).isValid(), "User1 should be able to add User2 as a friend");
        // user2 confirms
        assertTrue(friendService.confirmFriend(username2, username1).isValid(), "User2 should not be able to add User1 as a friend");
        // user1 -> user 3
        assertTrue(friendService.addFriend(username1, username3).isValid(), "User1 should be able to add User3 as a friend");
        // user3 confirms
        assertTrue(friendService.confirmFriend(username3, username1).isValid(), "User1 should be able to add User3 as a friend");
        // get the Friendships for each user
        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);

        // make sure there is the appropriate number of friendships
        assertEquals(2, user1Friends.size(), "User1 should only have 2 friend");
        assertEquals(1, user2Friends.size(), "User2 should only have 1 friend");
        assertEquals(1, user3Friends.size(), "User3 should only have 1 friends");

        // invalid tests
        final String invalidUsername = username1 + "FAKE";
        assertFalse(friendService.addFriend(username1, username2).isValid(), "User1 should not be able to friend User2 twice");
        assertFalse(friendService.addFriend(username1, null).isValid(), "User1 should not be able to friend 'null'");
        assertFalse(friendService.addFriend(null, null).isValid(), "'null' should not be able to friend 'null'");
        assertFalse(friendService.addFriend(username1, invalidUsername).isValid(), "User1 should not be able to friend a user who doesn't exist");
        assertFalse(friendService.addFriend(username1, username1).isValid(), "User1 should not be able to friend themselves");

        // get the Friendship tests again
        user1Friends = friendService.getFriends(username1);
        user2Friends = friendService.getFriends(username2);
        user3Friends = friendService.getFriends(username3);

        // make sure they are appropriate number of friends post-tests
        assertEquals(2, user1Friends.size(), "User1 should still only have 1 friend");
        assertEquals(1, user2Friends.size(), "User2 should still only have 1 friend");
        assertEquals(1, user3Friends.size(), "User3 should still only have 1 friends");

    }

    @Test
    public void removeFriendTest() {
        friendService.addFriend(username1, username2);
        friendService.addFriend(username2, username1);

        assertTrue(friendService.removeFriend(username1, username2).isValid(), "User1 should be able to unfriend User2");
        // a fake username
        final String invalidUsername = username1 + "FAKE";

        assertFalse(friendService.removeFriend(username1, username2).isValid(), "User1 should not be able to unfriend User2 twice");
        assertFalse(friendService.removeFriend(username1, null).isValid(), "User1 should not be able to unfriend 'null'");
        assertFalse(friendService.removeFriend(null, null).isValid(), "'null' should not be able to unfriend 'null'");
        assertFalse(friendService.removeFriend(username1, invalidUsername).isValid(), "User1 should not be able to unfriend a user who doesn't exist");
        assertFalse(friendService.removeFriend(username1, username1).isValid(), "User1 should not be able to unfriend themselves");
    }

    @Test
    public void getFriendTest() {

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
