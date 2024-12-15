package com.truecaller.controllers;

import com.truecaller.telegram.BotInitializer;
import com.truecaller.telegram.WebhookBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
@RequestMapping("/truecaller_api/webhook")
@RestController
public class WebhookBotController {
    private Logger logger = LoggerFactory.getLogger(WebhookBotController.class);
    @PostMapping("/{companyName}")
    public BotApiMethod<?> onUpdateReceived(@PathVariable String companyName,@RequestBody Update update){
        WebhookBot webhookBot = BotInitializer.botRegistry.get(companyName);
        logger.info(BotInitializer.botRegistry.toString());
        // Check if the bot exists
        if (webhookBot == null) {
            logger.error("No bot found for company: {}", companyName);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Bot not found for company: " + companyName
            );
        }
        // Delegate the update to the appropriate bot
        try {
            return webhookBot.onWebhookUpdateReceived(update);
        } catch (Exception e) {
            logger.error("Error processing update for company: {}", companyName, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process update"
            );
        }
    }
}