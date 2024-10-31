package com.company.companyapp.model;

import com.company.companyapp.DTO.CallerID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
public class Company {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private CallerID owner;
    private String description;
    private int followers;
    private double rating; // New field for storing average rating
    public Company(){}
    public Company(String id, String name, CallerID owner, String description, int followers, double rating) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.followers = followers;
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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
