package org.warrio.DTOs;

public class TokenSet {
    public String accessToken;
    public String refreshToken;
    public TokenSet(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
