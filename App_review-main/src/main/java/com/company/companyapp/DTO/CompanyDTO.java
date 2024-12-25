package com.company.companyapp.DTO;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class CompanyDTO {
    private String id;
    private String name;
    private String ownerEmail;
    private String description;
    private double rating;

    public CompanyDTO() {
    }

    public CompanyDTO(String id, String name, String ownerEmail, String description, double rating) {
        this.id = id;
        this.name = name;
        this.ownerEmail = ownerEmail;
        this.description = description;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
