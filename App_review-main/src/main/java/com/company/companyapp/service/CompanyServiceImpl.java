package com.company.companyapp.service;

import com.company.companyapp.model.Company; // Ensure you have the correct import
import com.company.companyapp.repository.CompanyRepository; // Import the repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {


    @Autowired
    private CompanyRepository companyRepository; // Inject the repository


    // New method to fetch all company names
    public List<String> getAllCompanyNames() {
        List<String> companyNames = companyRepository.findAll().stream()
                .map(Company::getName) // Extract company names
                .collect(Collectors.toList());
        for (String companyName : companyNames) {
            System.out.println(companyName);
        }
        return companyNames;
    }

    @Override
    public Optional<Company> getCompanyByName(String companyName) {
        return companyRepository.findByName(companyName);
    }
}