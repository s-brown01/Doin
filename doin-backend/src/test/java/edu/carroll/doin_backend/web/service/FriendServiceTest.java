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
    public void addFriendSuccessTest() {
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
    public void addFriendBadInputTest() {
        final String invalidUsername = username1 + "FAKE";

        // User1 tries to friend User2 again
        assertFalse(friendService.addFriend(username1, username2).isValid(), "User1 should not be able to friend User2 twice");

        // User1 tries to friend a non-existent user
        assertFalse(friendService.addFriend(username1, invalidUsername).isValid(), "User1 should not be able to friend a user who doesn't exist");

        // User1 tries to friend themselves
        assertFalse(friendService.addFriend(username1, username1).isValid(), "User1 should not be able to friend themselves");

        // Verify friendships remain unchanged after invalid attempts
        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);

        assertEquals(2, user1Friends.size(), "User1 should still have 2 friends");
        assertEquals(1, user2Friends.size(), "User2 should still have 1 friend");
        assertEquals(1, user3Friends.size(), "User3 should still have 1 friend");
    }

    @Test
    public void addFriendCrazyInputTest() {
        // User1 tries to friend null
        assertFalse(friendService.addFriend(username1, null).isValid(), "User1 should not be able to friend 'null'");

        // Both users are null
        assertFalse(friendService.addFriend(null, null).isValid(), "'null' should not be able to friend 'null'");

        // User2 tries to friend null
        assertFalse(friendService.addFriend(null, username1).isValid(), "'null' should not be able to friend User1");

        // Verify friendships remain unchanged after null attempts
        Set<FriendshipDTO> user1Friends = friendService.getFriends(username1);
        Set<FriendshipDTO> user2Friends = friendService.getFriends(username2);
        Set<FriendshipDTO> user3Friends = friendService.getFriends(username3);

        assertEquals(2, user1Friends.size(), "User1 should still have 2 friends");
        assertEquals(1, user2Friends.size(), "User2 should still have 1 friend");
        assertEquals(1, user3Friends.size(), "User3 should still have 1 friend");
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
