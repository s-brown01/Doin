package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.model.Image;

import java.util.Objects;

public class FriendshipDTO {
    private int id;
    private String username;
    private FriendshipStatus status;
    private Image profilePic;

    public FriendshipDTO(int id, String username, FriendshipStatus status, Image profilePic) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.profilePic = profilePic;
    }

    public FriendshipDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public Image getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(id, username, status);
    }


    @Override
    public String toString(){
        return "Friend: " + username + " with status " + status.toString();
    }


}
