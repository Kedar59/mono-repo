package com.truecaller.config;


import com.truecaller.services.OtpService;
import com.truecaller.services.ProfileService;
import com.truecaller.services.ReviewBotStateService;
import com.truecaller.telegram.WebhookBot;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;


@Configuration
public class Config {
    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Autowired
    private ProfileService profileService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private ReviewBotStateService reviewBotStateService;

    @Bean
    @Scope("prototype") // Ensure a new instance is created for each bot
    public WebhookBot webhookBot(String botToken) {
        return new WebhookBot(botToken,reviewBotStateService, profileService, otpService);
    }
    @Bean
    public String string(){
        return new String();
    }
}

