package com.company.companyapp.controller;

import com.company.companyapp.error.ErrorResponse;
import com.company.companyapp.model.Company;
import com.company.companyapp.model.CompanyBot;
import com.company.companyapp.service.CompanyBotService;
import com.company.companyapp.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companyBot")
public class CompanyBotController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyBotService companyBotService;
    @PostMapping("/addCompanyBot")
    public ResponseEntity<?> addCompanyBot(@RequestBody CompanyBot companyBot){
        Optional<CompanyBot> companyBotOptional = companyBotService.getCompanyBotByName(companyBot.getCompanyName());
        Optional<Company> companyOptional = companyService.getCompanyByName(companyBot.getCompanyName());
        if(companyBotOptional.isPresent()){
            return ResponseEntity.ok(new ErrorResponse(LocalDateTime.now(),"Company has already registered bot","Bot already exists"));
        } else if(!companyOptional.isPresent()){
            return ResponseEntity.ok(new ErrorResponse(LocalDateTime.now(),"Company not registered","Company doesn't exist"));
        }
        return ResponseEntity.ok(companyBotService.createCompanyBot(companyBot));
    }
    @GetMapping("/allBots")
    public ResponseEntity<List<CompanyBot>> getAllCompanyBots(){
        return ResponseEntity.ok(companyBotService.getAllBots());
    }

}
