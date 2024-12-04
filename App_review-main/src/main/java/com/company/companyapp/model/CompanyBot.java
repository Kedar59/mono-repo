package com.company.companyapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "companybots")  // MongoDB collection name
public class CompanyBot {

    @Id
    private String id; // Unique identifier for CompanyBot

    @DBRef
    private Company company; // Reference to Company collection

    private String botToken;
    private String botUrl;

    // Constructors, Getters, and Setters

    public CompanyBot() {
    }

    public CompanyBot(Company company, String botToken, String botUrl) {
        this.company = company;
        this.botToken = botToken;
        this.botUrl = botUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getBotUrl() {
        return botUrl;
    }

    public void setBotUrl(String botUrl) {
        this.botUrl = botUrl;
    }
}
