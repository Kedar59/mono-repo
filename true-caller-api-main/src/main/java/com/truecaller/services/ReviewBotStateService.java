package com.truecaller.services;

import com.truecaller.entities.ReviewBotState;
import com.truecaller.projections.Review;
import com.truecaller.telegram.BotState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewBotStateService {
    @Autowired
    MongoTemplate mongoTemplate;
    public void saveOrUpdateBotState(String companyName, Long chatId, BotState state, Review review) {
        String id = companyName + ":" + chatId;

        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update()
                .set("companyName", companyName)
                .set("chatId", chatId)
                .set("state", state)
                .set("review", review);

        mongoTemplate.upsert(query, update, ReviewBotState.class);
    }
    public ReviewBotState getBotState(String companyName, Long chatId) {
        String id = companyName + ":" + chatId;

        Query query = new Query(Criteria.where("id").is(id));
        ReviewBotState botState = mongoTemplate.findOne(query, ReviewBotState.class);

        // Return a default state if none is found
        if (botState == null) {
            botState = new ReviewBotState( companyName, chatId, BotState.IDLE, new Review(companyName));
        }
        return botState;
    }
    public void deleteBotState(String companyName, Long chatId) {
        String id = companyName + ":" + chatId;

        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, ReviewBotState.class);
    }
}
