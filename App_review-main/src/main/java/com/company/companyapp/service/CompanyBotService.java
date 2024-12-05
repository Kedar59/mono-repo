package com.company.companyapp.service;

import com.company.companyapp.model.Company;
import com.company.companyapp.model.CompanyBot;
import com.company.companyapp.repository.CompanyBotRepository;
import com.company.companyapp.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyBotService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyBotRepository companyBotRepository;

    public CompanyBot createCompanyBot(CompanyBot companyBot) {
        return companyBotRepository.save(companyBot);
    }
    public Optional<CompanyBot> getCompanyBotByName(String companyName){
        return companyBotRepository.findByCompanyName(companyName);
    }
    public List<CompanyBot> getAllBots(){
        return companyBotRepository.findAll();
    }
}
