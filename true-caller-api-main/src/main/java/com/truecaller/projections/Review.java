package com.truecaller.projections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

//@Document(collection = "reviews")
public class Review {

//    @Id
    private String id;
    private String companyName;  // Link to the company
    private CallerID reviewer;
    private String review;
    private float rating;
    private Date timestamp;

    public Review() {
    }

    // Constructor
    public Review(String companyName, CallerID reviewer, String review,float rating) {
        this.companyName = companyName;
        this.reviewer = reviewer;
        this.review = review;
        this.rating = rating;
    }
    public Review(String companyName,CallerID reviewer){
        this.companyName = companyName;
        this.reviewer = reviewer;
        this.rating = 3;
    }
    public Review(String companyName){
        this.companyName = companyName;
    }
    public Review(String id, String companyName, CallerID reviewer, String review, float rating, Date timestamp) {
        this.id = id;
        this.companyName = companyName;
        this.reviewer = reviewer;
        this.review = review;
        this.rating = rating;
        this.timestamp = timestamp;
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

    public CallerID getReviewer() {
        return reviewer;
    }

    public void setReviewer(CallerID reviewer) {
        this.reviewer = reviewer;
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

    @Override
    public String toString() {
        return "Review{" +
                "companyName='" + companyName + '\'' +
                ", reviewer=" + reviewer +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}

