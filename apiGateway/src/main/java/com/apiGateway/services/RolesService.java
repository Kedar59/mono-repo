package com.apiGateway.services;

import com.apiGateway.entities.Roles;
import com.apiGateway.repositories.RolesRepository;
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
