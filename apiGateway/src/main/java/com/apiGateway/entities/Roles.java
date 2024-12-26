package com.apiGateway.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Roles")
public class Roles {
    @Id
    private String id;
    private String profileId;
    private String companyId;
    private Role role;
    public Roles(String id, String profileId, String companyId, Role role) {
        this.id = id;
        this.profileId = profileId;
        this.companyId = companyId;
        this.role = role;
    }
    public Roles(String profileId, String companyId, Role role) {
        this.profileId = profileId;
        this.companyId = companyId;
        this.role = role;
    }
    public Roles(){}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
