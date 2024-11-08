package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.model.User;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) for the User entity.
 * <p>
 * This class is used to transfer user data, including user ID, username, and profile picture.
 * It provides methods to access and modify these properties, as well as methods for equality comparison and hash code generation.
 */
public class UserDTO {
    private Integer id;
    private String username;
    private Image profilePicture;

    /**
     * Constructs a UserDTO based on a User entity.
     * This constructor copies the necessary properties from the User object to create a UserDTO.
     *
     * @param user the User entity to create the UserDTO from.
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture();
    }
    public UserDTO(){}
    /**
     * Gets the profile picture of the user.
     *
     * @return the profile picture image associated with the user.
     */
    public Image getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the profile picture of the user.
     *
     * @param profilePictureId the new profile picture image to associate with the user.
     */
    public void setProfilePicture(Image profilePictureId) {
        this.profilePicture = profilePictureId;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username associated with the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username to set for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the ID of the user.
     *
     * @return the unique ID of the user.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id the new ID to assign to the user.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Compares this UserDTO to another object for equality.
     *
     * @param o the object to compare this UserDTO to.
     * @return true if the other object is a UserDTO with the same id, username, and profilePicture; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;
        return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username) && Objects.equals(profilePicture, userDTO.profilePicture);
    }

    /**
     * Returns the hash code for this UserDTO.
     *
     * @return the hash code based on the id, username, and profile picture of the user.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, profilePicture);
    }
}
