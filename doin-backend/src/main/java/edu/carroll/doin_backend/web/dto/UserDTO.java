package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.model.User;

public class UserDTO {
    public Image getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(Image profilePictureId) {
        this.profilePictureId = profilePictureId;
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

    private Integer id;
    private String username;
    private Image profilePictureId;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePictureId = user.getProfilePictureId();
    }

    public UserDTO() {}
}
