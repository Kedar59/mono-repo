package com.truecaller.controllers;

import com.truecaller.telegram.WebhookBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookBotController {
    private final WebhookBot webHookBot;
    private Logger logger = LoggerFactory.getLogger(WebhookBotController.class);

    @Autowired
    public WebhookBotController(WebhookBot siparisVerBot){
        this.webHookBot=siparisVerBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        logger.info("Update received. Id: {}", update.getUpdateId());
        return webHookBot.onWebhookUpdateReceived(update);
    }
}
