package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.model.User;

import java.util.Objects;

public class UserDTO {
    private Integer id;
    private String username;
    private Image profilePicture;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture();
    }

    public UserDTO() {
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePictureId) {
        this.profilePicture = profilePictureId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;
        return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username) && Objects.equals(profilePicture, userDTO.profilePicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, profilePicture);
    }
}
