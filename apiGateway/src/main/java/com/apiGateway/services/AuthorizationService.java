package com.apiGateway.services;

import com.apiGateway.entities.Role;
import com.apiGateway.entities.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Autowired
    private RolesService rolesService;

    public boolean isAdmin(String profileId, String companyId) {
        return rolesService.verify(new Roles(profileId,companyId,Role.ADMIN));
    }

    public boolean isModerator(String profileId, String companyId) {
        return rolesService.verify(new Roles(profileId,companyId,Role.MODERATOR));
    }

    public boolean isNormal(String profileId, String companyId) {
        return rolesService.verify(new Roles(profileId,companyId,Role.NORMAL));
    }
}