package org.warrio.DTOs;

public class UserProfile {
    public String username;
    public boolean isActivated;
    public UserProfile(String username, boolean isActivated){
        this.username = username;
        this.isActivated = isActivated;
    }

}
