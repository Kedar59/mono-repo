package com.company.companyapp.repository;

import com.company.companyapp.model.Company;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends MongoRepository<Company, String> {
    Optional<Company> findByNameContainingIgnoreCase(String name);

    // New method to fetch all company names
    List<Company> findAll();  // This method is already provided by MongoRepository

    Optional<Company> findByName(String companyName);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Company> findByNameContaining(String name);
    Optional<Company> findCompanyById(String Id);
}

