package com.apiGateway.entities;

import com.apiGateway.DTO.ProfileDTO;

public class LoginResponse {
    private String jwtToken;
    private ProfileDTO profile;

    public LoginResponse(String jwtToken, ProfileDTO profile) {
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

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }
}
