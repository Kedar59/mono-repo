package com.company.companyapp.service;

import com.company.companyapp.model.Roles;
import com.company.companyapp.repository.RolesRepository;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;
    public Optional<Roles> findUsersRoleInCompany(String profileId, String companyId){
        return rolesRepository.findByProfileIdAndCompanyId(profileId,companyId);
    }
    public Roles save(Roles roles){
        return rolesRepository.save(roles);
    }
    public boolean verify(Roles roles){
        return rolesRepository.existsByProfileIdAndCompanyIdAndRole(roles.getProfileId(),roles.getCompanyId(),roles.getRole());
    }
}
