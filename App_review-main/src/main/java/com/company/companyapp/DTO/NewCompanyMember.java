package com.company.companyapp.DTO;

public class NewCompanyMember {
    private String email;
    public NewCompanyMember(){}
    public NewCompanyMember(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
