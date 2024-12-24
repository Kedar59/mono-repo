package com.company.companyapp.DTO;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class CompanyDTO {
    private String id;
    private String name;
    private CallerID owner;
    private String description;
    private double rating;
    public CompanyDTO(){}

    public CompanyDTO(String id, String name, CallerID owner, String description, double rating) {
        this.id = id;
        this.name = name;
        this.owner = owner;
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

    public CallerID getOwner() {
        return owner;
    }

    public void setOwner(CallerID owner) {
        this.owner = owner;
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
