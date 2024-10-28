package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.model.User;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing a simplified view of a user's friendship information.
 * <p>
 * This class includes the friend's ID, username, friendship status, and profile picture.
 * Primarily used to transfer friend-related data between different application layers without
 * exposing the full {@link User} model.
 * </p>
 */
public class FriendshipDTO {

    private int id;
    private String username;
    private FriendshipStatus status;
    private Image profilePic;

    /**
     * Constructs a new {@code FriendshipDTO} with specified values for all fields.
     *
     * @param id         the unique identifier of the friend
     * @param username   the username of the friend
     * @param status     the friendship status with the current user
     * @param profilePic the profile picture of the friend
     */
    public FriendshipDTO(int id, String username, FriendshipStatus status, Image profilePic) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.profilePic = profilePic;
    }

    /**
     * Default constructor for creating an empty {@code FriendshipDTO}.
     */
    public FriendshipDTO() {
    }

    /**
     * Returns the unique identifier of the friend.
     *
     * @return the friend's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the friend.
     *
     * @param id the friend's ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the username of the friend.
     *
     * @return the friend's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the friend.
     *
     * @param username the friend's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the friendship status with the current user.
     *
     * @return the friendship status
     */
    public FriendshipStatus getStatus() {
        return status;
    }

    /**
     * Sets the friendship status with the current user.
     *
     * @param status the friendship status
     */
    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    /**
     * Returns the profile picture of the friend.
     *
     * @return the profile picture
     */
    public Image getProfilePic() {
        return profilePic;
    }

    /**
     * Sets the profile picture of the friend.
     *
     * @param profilePic the friend's profile picture
     */
    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing friend ID,
     * username, and friendship status.
     *
     * @param o the reference object with which to compare
     * @return {@code true} if this object is the same as the {@code o} argument;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FriendshipDTO that = (FriendshipDTO) o;
        return id == that.id &&
                username.equals(that.getUsername()) &&
                status == that.status;
    }

    /**
     * Returns a hash code value for the object, based on the friend ID, username, and
     * friendship status.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, status);
    }

    /**
     * Returns a string representation of the {@code FriendshipDTO} object,
     * including the friend's username and friendship status.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Friend: " + username + " with status " + status;
    }
}
