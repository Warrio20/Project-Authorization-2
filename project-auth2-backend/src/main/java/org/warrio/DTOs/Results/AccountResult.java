package org.warrio.DTOs.Results;

import org.warrio.DTOs.UserDTO;

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
