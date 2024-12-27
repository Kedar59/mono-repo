package com.truecaller.projections;

public class ProfileWithoutContact {
    private String id;
    private String email;
    private String name;
    private boolean isVerified;
    private String location;
    private int numberOfSpamCallReports;
    private int numberOfSpamSMSReports;
    public ProfileWithoutContact(){}
    public ProfileWithoutContact(String id, String email, String name, boolean isVerified, String location, int numberOfSpamCallReports, int numberOfSpamSMSReports) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.isVerified = isVerified;
        this.location = location;
        this.numberOfSpamCallReports = numberOfSpamCallReports;
        this.numberOfSpamSMSReports = numberOfSpamSMSReports;
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

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfSpamCallReports() {
        return numberOfSpamCallReports;
    }

    public void setNumberOfSpamCallReports(int numberOfSpamCallReports) {
        this.numberOfSpamCallReports = numberOfSpamCallReports;
    }

    public int getNumberOfSpamSMSReports() {
        return numberOfSpamSMSReports;
    }

    public void setNumberOfSpamSMSReports(int numberOfSpamSMSReports) {
        this.numberOfSpamSMSReports = numberOfSpamSMSReports;
    }
}