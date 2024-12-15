package com.truecaller.projections;

import com.truecaller.entities.Profile;

public class AuthenticationResponse {
    private String token;
    private boolean authenticated;
    private String message;
    private Profile profile;

    public AuthenticationResponse(String token, boolean authenticated, String message, Profile profile) {
        this.token = token;
        this.authenticated = authenticated;
        this.message = message;
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    // Constructors, getters, setters
    public AuthenticationResponse(){}

    public AuthenticationResponse(String token, boolean authenticated, String message) {
        this.token = token;
        this.authenticated = authenticated;
        this.message = message;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
