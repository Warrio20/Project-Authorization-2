package org.warrio.DTOs;


import org.bson.Document;

public class UserDTO {
    public String email;
    public String username;
    public UserDTO(Document document){
        this(document.getString("email"), document.getString("username"));
    }
    public UserDTO(String email, String username){
        this.email = email;
        this.username = username;
    }

}
