package org.warrio.DTOs;


import org.bson.Document;

public class UserDTO {
    public String email;
    public String username;
    public boolean isActivated;
    public UserDTO(Document document){
        this(document.getString("email"), document.getString("username"), document.getBoolean("isActivated"));
    }
    public UserDTO(String email, String username, boolean isActivated){
        this.email = email;
        this.username = username;
        this.isActivated = isActivated;
    }

}
