package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.FriendshipDTO;
import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.model.Friendship;
import edu.carroll.doin_backend.web.model.SecurityQuestion;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.FriendRepository;
import edu.carroll.doin_backend.web.repository.LoginRepository;
import edu.carroll.doin_backend.web.repository.SecurityQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class FriendServiceTest {
    private static final String username1 = "User1_Username";
    private static final String username2 = "User2_Username";
    private static final String username3 = "User3_Username";

    @Autowired
    private FriendService friendService;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityQuestionRepository securityQuestionRepo;
    @Autowired
    private FriendRepository friendRepository;

    @BeforeEach
    public void loadTables() {
        loginRepository.deleteAll();
        securityQuestionRepo.deleteAll();

        securityQuestionRepo.save(new SecurityQuestion(1, "pet"));
        loginRepository.save(createNewUser(username1));
        loginRepository.save(createNewUser(username2));
        loginRepository.save(createNewUser(username3));
    }

    private User createNewUser(String username) {
        RegisterDTO data = new RegisterDTO(username, "password", "pet", "answer");
        User newUser = new User(data, "password", securityQuestionRepo.findByQuestion("pet").get(0));
        return newUser;
    }

    @Test
    public void getFriendsOfFriendsTest() {
        // get all 3 users
        User user1 = loginRepository.findByUsernameIgnoreCase(username1).get(0);
        User user2 = loginRepository.findByUsernameIgnoreCase(username2).get(0);
        User user3 = loginRepository.findByUsernameIgnoreCase(username3).get(0);

        // add the friendships into the repository (assuming they work)
        // user1 -> user2 - user3
        // CHANGE TO friendService.addFriend(username1, username2);
        friendRepository.save(new Friendship(user1, user2, FriendshipStatus.CONFIRMED));
        friendRepository.save(new Friendship(user3, user2, FriendshipStatus.CONFIRMED));

        // get the friends of friends for user 1 - should be User 3
        FriendshipDTO[] friendsOfUser1 = friendService.getFriendsOfFriends(username1);
        FriendshipDTO[] friendsOfUser3 = friendService.getFriendsOfFriends(username3);

        assertEquals(1, friendsOfUser1.length, "User 1 should only have 1 friend of friend");
        assertEquals(1, friendsOfUser3.length, "User 3 should only have 1 friend of friend");

        assertEquals(username3, friendsOfUser1[0].getUsername(), "User 3 should be User1's mutual");
        assertEquals(username1, friendsOfUser3[0].getUsername(), "User 1 should be User3's mutual");
    }

    @Test
    public void addFriendTest() {
        assertTrue(friendService.addFriend(username1, username2), "User1 should be able to add User2 as a friend");
        assertTrue(friendService.addFriend(username1, username3), "User1 should be able to add User3 as a friend");
        assertTrue(friendService.addFriend(username2, username1), "User2 should be able to add User1 as a friend");
        // get the Friendships for each user
        Set<Friendship> user1Friends = friendRepository.findByUser(loginRepository.findByUsernameIgnoreCase(username1).get(0));
        Set<Friendship> user2Friends = friendRepository.findByUser(loginRepository.findByUsernameIgnoreCase(username2).get(0));
        Set<Friendship> user3Friends = friendRepository.findByUser(loginRepository.findByUsernameIgnoreCase(username3).get(0));

        // make sure there is the appropriate number of friendships
        assertEquals(2, user1Friends.size(), "User1 should only have 1 friend");
        assertEquals(1, user2Friends.size(), "User2 should only have 1 friend");
        assertEquals(0, user3Friends.size(), "User3 should have 0 friends");

        // invalid tests
        final String invalidUsername = username1 + "FAKE";
        assertFalse(friendService.addFriend(username1, username2), "User1 should not be able to friend User2 twice");
        assertFalse(friendService.addFriend(username1, null), "User1 should not be able to friend 'null'");
        assertFalse(friendService.addFriend(null, null), "'null' should not be able to friend 'null'");
        assertFalse(friendService.addFriend(username1, invalidUsername), "User1 should not be able to friend a user who doesn't exist");
        assertFalse(friendService.addFriend(username1, username1), "User1 should not be able to friend themselves");

        // get the Friendship tests again
        user1Friends = friendRepository.findByUser(loginRepository.findByUsernameIgnoreCase(username1).get(0));
        user2Friends = friendRepository.findByUser(loginRepository.findByUsernameIgnoreCase(username2).get(0));
        user3Friends = friendRepository.findByUser(loginRepository.findByUsernameIgnoreCase(username3).get(0));

        // make sure they are appropriate number of friends post-tests
        assertEquals(2, user1Friends.size(), "User1 should still only have 1 friend");
        assertEquals(1, user2Friends.size(), "User2 should still only have 1 friend");
        assertEquals(0, user3Friends.size(), "User3 should still have 0 friends");

    }

    @Test
    public void removeFriendTest() {
        friendService.addFriend(username1, username2);
        friendService.addFriend(username2, username1);

        assertTrue(friendService.removeFriend(username1, username2), "User1 should be able to unfriend User2");
        // a fake username
        final String invalidUsername = username1 + "FAKE";

        assertFalse(friendService.removeFriend(username1, username2), "User1 should not be able to unfriend User2 twice");
        assertFalse(friendService.removeFriend(username1, null), "User1 should not be able to unfriend 'null'");
        assertFalse(friendService.removeFriend(null, null), "'null' should not be able to unfriend 'null'");
        assertFalse(friendService.removeFriend(username1, invalidUsername), "User1 should not be able to unfriend a user who doesn't exist");
        assertFalse(friendService.removeFriend(username1, username1), "User1 should not be able to unfriend themselves");




    }

    @Test
    public void getFriendTest() {

    }
}
