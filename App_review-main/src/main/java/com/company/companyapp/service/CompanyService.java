package com.company.companyapp.service;

import com.company.companyapp.DTO.CompanyDTO;
import com.company.companyapp.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<String> getAllCompanyNames();

    Optional<Company> getCompanyByName(String companyName);

    List<CompanyDTO> getAllCompanies();

    List<CompanyDTO> searchForCompanies(String companyName);

    Company save(Company company);

    List<Company> findAll();
}