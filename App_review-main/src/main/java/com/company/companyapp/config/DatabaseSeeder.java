package com.company.companyapp.config;

import com.company.companyapp.model.Company;
import com.company.companyapp.model.CompanyBot;
import com.company.companyapp.repository.CompanyBotRepository;
import com.company.companyapp.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyBotRepository companyBotRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if the company collection is empty and insert a sample company
        Company testCompany = companyRepository.findByName("TestCompany")
                .orElseGet(() -> companyRepository.save(new Company(null, "TestCompany", null, "A test company", 100, 4.5)));

        // Check if the companybots collection is empty and insert a sample bot
        if (companyBotRepository.count() == 0) {
            CompanyBot companyBot = new CompanyBot(testCompany, "testBotToken", "https://t.me/test_bot");
            companyBotRepository.save(companyBot);
            System.out.println("Sample CompanyBot entity inserted.");
        }
    }
}

