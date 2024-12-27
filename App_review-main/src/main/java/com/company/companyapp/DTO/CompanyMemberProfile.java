package com.company.companyapp.DTO;

import com.company.companyapp.model.Role;

public class CompanyMemberProfile {
    private ProfileWithoutContact profile;
    private Role role;
    public CompanyMemberProfile(){}
    public CompanyMemberProfile(ProfileWithoutContact profile, Role role) {
        this.profile = profile;
        this.role = role;
    }

    public ProfileWithoutContact getProfile() {
        return profile;
    }

    public void setProfile(ProfileWithoutContact profile) {
        this.profile = profile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
