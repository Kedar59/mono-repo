package com.company.companyapp.controller;

import com.company.companyapp.error.ErrorResponse;
import com.company.companyapp.model.Company;
import com.company.companyapp.model.CompanyBot;
import com.company.companyapp.service.CompanyBotService;
import com.company.companyapp.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/review_api/companyBot")
public class CompanyBotController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyBotService companyBotService;
    private Logger logger = LoggerFactory.getLogger(CompanyBotController.class);
    @PostMapping("/profile/{profileId}/company/{companyId}/addCompanyBot")
    public ResponseEntity<?> addCompanyBot(@RequestBody CompanyBot companyBot){
        logger.info("IN add bot : ");
        Optional<CompanyBot> companyBotOptional = companyBotService.getCompanyBotByName(companyBot.getCompanyName());
        Optional<Company> companyOptional = companyService.getCompanyByName(companyBot.getCompanyName());
        if(companyBotOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(new ErrorResponse(LocalDateTime.now(),"Company has already registered bot","Bot already exists"));
        } else if(!companyOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(new ErrorResponse(LocalDateTime.now(),"Company not registered","Company doesn't exist"));
        }
        return ResponseEntity.ok(companyBotService.createCompanyBot(companyBot));
    }
    @GetMapping("/allBots")
    public ResponseEntity<List<CompanyBot>> getAllCompanyBots(){
        logger.info("In all bots hit");
        return ResponseEntity.ok(companyBotService.getAllBots());
    }

    @GetMapping("/companyBotMenu")
    public ResponseEntity<List<String>> getCompanyMenu() {
        // Retrieve all bots
        List<CompanyBot> bots = companyBotService.getAllBots();

        // Map company names and bot URLs to the required format
        List<String> menu = bots.stream()
                .map(bot -> bot.getCompanyName() + ": " + bot.getBotUrl())
                .collect(Collectors.toList());

        return ResponseEntity.ok(menu);
    }
}
