package com.truecaller.telegram;

import com.truecaller.projections.CompanyBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class BotInitializer {
//    @Bean
    public static HashMap<String,WebhookBot> botRegistry;
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
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApplicationContext applicationContext;
    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        logger.info("Initializing bot with webhookPath: {}", webhookPath);
        logger.info("Bot username: {}", botUsername);
        CompanyBot[] listCompanyBots = restTemplate.getForObject("http://localhost:8081/api/companyBot/allBots", CompanyBot[].class);
        assert listCompanyBots != null;
        List<CompanyBot> listBots = Arrays.stream(listCompanyBots).toList();
        logger.info("list of bots : "+listBots.toString());
        botRegistry = new HashMap<String,WebhookBot>();
        for(CompanyBot bot : listBots){
            try {
                WebhookBot webhookBot = applicationContext.getBean(WebhookBot.class, bot.getBotToken());
                webhookBot.setBotUsername(bot.getCompanyName());
                webhookBot.setBotToken(bot.getBotToken());
                webhookBot.setBotPath(webhookPath+"/"+bot.getCompanyName());
//                webhookBot.setWebhook(SetWebhook.builder().url(webhookBot.getBotPath()).build());
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(webhookBot, SetWebhook.builder().url(webhookBot.getBotPath()).build());
                logger.info("Webhook successfully registered!");
                botRegistry.put(bot.getCompanyName(),webhookBot);
            } catch (TelegramApiException e) {
                logger.error("Failed to register webhook: ", e);
            }
        }

    }
}
