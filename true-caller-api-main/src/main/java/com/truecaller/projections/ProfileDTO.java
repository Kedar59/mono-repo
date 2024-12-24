package com.truecaller.projections;

import com.truecaller.entities.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

public class ProfileDTO {
    private String id;
    private String email;

    private String phoneNumber;

    private String countryCode;

    private String name;
    private boolean isVerified;

    private String location;
    private int numberOfSpamCallReports;
    private int numberOfSpamSMSReports;
    public ProfileDTO(){}
    public ProfileDTO(String id, String email, String phoneNumber, String countryCode, String name, boolean isVerified, String location, int numberOfSpamCallReports, int numberOfSpamSMSReports) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
