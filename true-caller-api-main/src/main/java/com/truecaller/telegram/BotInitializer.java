package com.truecaller.telegram;

import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {
    @Value("${telegrambot.botUsername}")
    private String botUsername;
    @Value("${telegrambot.botToken}")
    private String botToken;

    @Value("${telegrambot.webhookPath}")
    private String webhookPath;
    private final TelegramBot telegramBot;
    private Logger logger = LoggerFactory.getLogger(BotInitializer.class);
    public BotInitializer(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(webhookPath).build();
    }
    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        logger.info("Initializing bot with webhookPath: {}", webhookPath);
        logger.info("Bot username: {}", botUsername);
        try {
            WebhookBot webhookBot = new WebhookBot();
            webhookBot.setBotUsername(botUsername);
            webhookBot.setBotToken(botToken);
            webhookBot.setBotPath(webhookPath);
            webhookBot.setWebhook(setWebhookInstance());

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(webhookBot, setWebhookInstance());
            logger.info("Webhook successfully registered!");
        } catch (TelegramApiException e) {
            logger.error("Failed to register webhook: ", e);
        }
    }
}
