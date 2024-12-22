package com.apiGateway.DTO;

public class ProfileDTO {
    private String id;
    private String email;
    private String name;
    private Boolean verified;

    public ProfileDTO(){}
    public ProfileDTO(String id, String email, String name, Boolean verified) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.verified = verified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
