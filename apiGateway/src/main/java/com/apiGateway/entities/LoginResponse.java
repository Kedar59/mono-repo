package com.apiGateway.entities;

import com.apiGateway.DTO.User;

public class LoginResponse {
    private String jwtToken;
    private User profile;

    public LoginResponse(String jwtToken, User profile) {
        this.jwtToken = jwtToken;
        this.profile = profile;
    }
    public LoginResponse(){}

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public User getProfile() {
        return profile;
    }

    public void setProfile(User profile) {
        this.profile = profile;
    }
}
