package org.warrio.DTOs;

public class AccountResult {
    public final String accessToken;
    public final String refreshToken;
    public final UserDTO user;
    public AccountResult(String accessToken, String refreshToken, UserDTO user){
        this.accessToken = accessToken;
        this.user = user;
        this.refreshToken = refreshToken;
    }
}
