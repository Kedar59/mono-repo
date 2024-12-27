package com.company.companyapp.service;

import com.company.companyapp.DTO.CompanyDTO;
import com.company.companyapp.error.ErrorResponse;
import com.company.companyapp.model.Company; // Ensure you have the correct import
import com.company.companyapp.repository.CompanyBotRepository;
import com.company.companyapp.repository.CompanyRepository; // Import the repository
import com.company.companyapp.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository; // Inject the repository
    @Autowired
    private CompanyBotRepository companyBotRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Override
    public ResponseEntity<?> deleteCompany(String profileId, String companyId) {
        Optional<Company> existingCompany = companyRepository.findById(companyId);
        if(existingCompany.isEmpty()){
            return ResponseEntity.ok(new ErrorResponse(LocalDateTime.now(),"Company already dosent exist","Company with Id does'nt exist "+companyId));
        }
        Company company = existingCompany.get();
        companyBotRepository.deleteByCompanyName(company.getName());
        rolesRepository.deleteByCompanyId(companyId);
        companyRepository.deleteById(companyId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return ResponseEntity.ok(response);
    }

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

    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<Company> allCompanies = companyRepository.findAll();
        List<CompanyDTO> companyDTOs = new ArrayList<>();
        for (Company company : allCompanies) {
            companyDTOs.add(company.convertToDTO(company));
        }
        return companyDTOs;
    }

    @Override
    public List<CompanyDTO> searchForCompanies(String companyName) {
        List<Company> allCompanies = companyRepository.findByNameContaining(companyName);
        List<CompanyDTO> companyDTOs = new ArrayList<>();
        for (Company company : allCompanies) {
            companyDTOs.add(company.convertToDTO(company));
        }
        return companyDTOs;
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }
}