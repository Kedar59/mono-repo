package com.company.companyapp.model;

import com.company.companyapp.DTO.CompanyDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
public class Company {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String ownerEmail;
    private String description;
    // private int followers;
    private double rating; // New field for storing average rating
    private int numberOfReviews; // Add this field

    public CompanyDTO convertToDTO(Company company) {
        return new CompanyDTO(company.getId(), company.getName(), company.getOwnerEmail(), company.getDescription(),
                company.getRating(), company.getNumberOfReviews());
    }

    public Company() {
    }

    public Company(String id, String name, String ownerEmail, String description, double rating, int numberOfReviews) {
        this.id = id;
        this.name = name;
        this.ownerEmail = ownerEmail;
        this.description = description;
        // this.followers = followers;
        this.rating = rating;
        this.numberOfReviews = numberOfReviews;
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

    // public int getFollowers() {
    // return followers;
    // }
    //
    // public void setFollowers(int followers) {
    // this.followers = followers;
    // }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }
}
