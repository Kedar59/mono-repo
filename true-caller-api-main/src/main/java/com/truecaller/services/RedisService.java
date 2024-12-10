package com.truecaller.services;

import com.truecaller.TruecallerApplication;
import com.truecaller.entities.ReviewBotState;
import com.truecaller.projections.Review;
import com.truecaller.telegram.BotState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    private Logger logger = LoggerFactory.getLogger(RedisService.class);
    private static final Duration EXPIRATION = Duration.ofHours(1); // Example: 1-hour expiration for bot states

    // Save or update bot state
    public void saveOrUpdateBotState(String companyName, Long chatId, BotState state, Review review) {
        String redisKey = companyName + ":" + chatId;

        // Create a ReviewBotState object
        ReviewBotState botState = new ReviewBotState(companyName, chatId, state, review);

        // Save the object to Redis
        redisTemplate.opsForValue().set(redisKey, botState, EXPIRATION);
    }

    // Get bot state
    public ReviewBotState getBotState(String companyName, Long chatId) {
        String redisKey = companyName + ":" + chatId;

        // Retrieve the object from Redis
        ReviewBotState botState = (ReviewBotState) redisTemplate.opsForValue().get(redisKey);

        // Return a default state if none is found
        if (botState == null) {
            botState = new ReviewBotState(companyName, chatId, BotState.IDLE, new Review(companyName));
        }

        return botState;
    }

    // Delete bot state
    public void deleteBotState(String companyName, Long chatId) {
        String redisKey = companyName + ":" + chatId;

        // Delete the key from Redis
        redisTemplate.delete(redisKey);
    }
}
