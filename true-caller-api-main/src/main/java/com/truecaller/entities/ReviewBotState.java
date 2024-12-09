package com.truecaller.entities;


import com.truecaller.telegram.BotState;
import com.truecaller.projections.Review;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bot_states")
public class ReviewBotState {

    @Id
    private String id; // Composite key: companyName + ":" + chatId
    private String companyName;
    private Long chatId;
    private BotState state;
    private Review review;

    // Constructors
    public ReviewBotState(String companyName, Long chatId, BotState state, Review review) {
        this.id = companyName + ":" + chatId;
        this.companyName = companyName;
        this.chatId = chatId;
        this.state = state;
        this.review = review;
    }

    public ReviewBotState() {
    }

    public ReviewBotState(String id, String companyName, Long chatId, BotState state, Review review) {
        this.id = id;
        this.companyName = companyName;
        this.chatId = chatId;
        this.state = state;
        this.review = review;
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public BotState getState() {
        return state;
    }

    public void setState(BotState state) {
        this.state = state;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}