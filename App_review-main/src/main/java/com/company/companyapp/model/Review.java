package com.company.companyapp.model;

import com.company.companyapp.DTO.CallerID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;
    private String companyName; // Link to the company
    private String reviewerEmail;
    private String review;
    private float rating;
    private Date timestamp;

    public Review() {
    }

    // Constructor
    public Review(String companyName, String reviewerEmail, String review, float rating) {
        this.companyName = companyName;
        this.reviewerEmail = reviewerEmail;
        this.review = review;
        this.rating = rating;
    }

    public Review(String id, String companyName, String reviewerEmail, String review, float rating, Date timestamp) {
        this.id = id;
        this.companyName = companyName;
        this.reviewerEmail = reviewerEmail;
        this.review = review;
        this.rating = rating;
        this.timestamp = timestamp;
    }
    public Review(String companyName){
        this.companyName = companyName;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}