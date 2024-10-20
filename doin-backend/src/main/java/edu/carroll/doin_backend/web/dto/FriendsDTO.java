package edu.carroll.doin_backend.web.dto;

import java.util.Set;

public class FriendsDTO {
    private final Set<String> friendsNames;
    public FriendsDTO(Set<String> friendsNames) {
        this.friendsNames = friendsNames;
    }

    public Set<String> getFriendsNames() {
        return friendsNames;
    }
}
