package com.apiGateway.DTO;

public class AuthorizationRequest {
    private String profileId;
    private String companyId;
    public AuthorizationRequest(){}

    public AuthorizationRequest(String profileId, String companyId) {
        this.profileId = profileId;
        this.companyId = companyId;
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
}
