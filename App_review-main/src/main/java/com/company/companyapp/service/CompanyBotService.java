package com.company.companyapp.service;

import com.company.companyapp.model.Company;
import com.company.companyapp.model.CompanyBot;
import com.company.companyapp.repository.CompanyBotRepository;
import com.company.companyapp.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyBotService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyBotRepository companyBotRepository;

    public CompanyBot createCompanyBot(String companyName, String botToken, String botUrl) {
        // Fetch the company by name
        Optional<Company> companyOptional = companyRepository.findByName(companyName);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            CompanyBot companyBot = new CompanyBot(company, botToken, botUrl);
            return companyBotRepository.save(companyBot);
        } else {
            throw new IllegalArgumentException("Company with name " + companyName + " does not exist.");
        }
    }
}
