package com.truecaller.telegram;

public enum BotState {
    IDLE, // Default state, no active interaction
    AWAITING_REVIEW, // Waiting for the user's review text
    AWAITING_PHONE_NUMBER, // Waiting for the user's phone number
    AWAITING_RATING
}