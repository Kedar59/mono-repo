package com.truecaller.projections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "companybots")  // MongoDB collection name
public class CompanyBot {

    @Id
    private String id; // Unique identifier for CompanyBot

    @Indexed(unique = true)
    private String companyName; // Reference to Company collection
    private String botToken;
    private String botUsername;
    private String botUrl;

    // Constructors, Getters, and Setters

    public CompanyBot() {
    }

    public CompanyBot(String companyName, String botToken, String botUrl,String botUsername) {
        this.companyName=companyName;
        this.botToken = botToken;
        this.botUrl = botUrl;
        this.botUsername = botUsername;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
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

    @Override
    public String toString() {
        return "CompanyBot{" +
                "companyName='" + companyName + '\'' +
                ", botToken='" + botToken + '\'' +
                ", botUsername='" + botUsername + '\'' +
                ", botUrl='" + botUrl + '\'' +
                '}';
    }
}
