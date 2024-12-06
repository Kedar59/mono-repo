package com.company.companyapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class CompanyappApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "secrets");
        SpringApplication.run(CompanyappApplication.class, args);
    }

    @Bean
    CommandLineRunner testConnection(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                System.out.println("Connected to database: " + mongoTemplate.getDb().getName());
            } catch (Exception e) {
                System.err.println("Failed to connect to MongoDB:");
                e.printStackTrace();
            }
        };
    }
}
