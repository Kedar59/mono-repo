package com.apiGateway.DTO;

import com.apiGateway.entities.Role;

public class AuthorizationResponse {
    private Role role;
    public AuthorizationResponse(){}

    public AuthorizationResponse(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
