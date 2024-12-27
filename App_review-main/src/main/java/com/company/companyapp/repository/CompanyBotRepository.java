package com.company.companyapp.repository;


import com.company.companyapp.model.CompanyBot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyBotRepository extends MongoRepository<CompanyBot, String> {

    // Find a CompanyBot by companyName (assuming companyName is unique)
    Optional<CompanyBot> findByCompanyName(String companyName);

    // Fetch all CompanyBots (this is provided by MongoRepository)
    List<CompanyBot> findAll();

    // Find a CompanyBot by botToken
    Optional<CompanyBot> findByBotToken(String botToken);

    // Find a CompanyBot by botUrl
    Optional<CompanyBot> findByBotUrl(String botUrl);
    void deleteByCompanyName(String companyName);
}

