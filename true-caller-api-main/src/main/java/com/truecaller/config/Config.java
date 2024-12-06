package com.truecaller.config;


import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
<<<<<<< Updated upstream
=======

    @Autowired
    private ProfileService profileService;

    @Autowired
    private OtpService otpService;

    @Bean
    @Scope("prototype") // Ensure a new instance is created for each bot
    public WebhookBot webhookBot(String botToken) {
        return new WebhookBot(botToken, profileService, otpService);
    }
    @Bean
    public String string(){
        return new String();
    }
>>>>>>> Stashed changes
}

