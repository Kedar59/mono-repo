package com.company.companyapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.generics.TelegramBot;

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


}
