package com.apiGateway.controller;

import com.apiGateway.DTO.AuthorizationRequest;
import com.apiGateway.DTO.AuthorizationResponse;
import com.apiGateway.entities.Role;
import com.apiGateway.entities.Roles;
import com.apiGateway.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/gateway/roles")
public class RolesController {
    @Autowired
    private RolesService rolesService;
    @PostMapping("/findRole")
    public ResponseEntity<?> getCompanyProfileRelation(@RequestBody AuthorizationRequest authorizationRequest){
        Optional<Roles> roles = rolesService.findUsersRoleInCompany(authorizationRequest.getProfileId(), authorizationRequest.getCompanyId());
        if(roles.isEmpty()){
            return ResponseEntity.ok(new AuthorizationResponse(Role.NORMAL));
        }
        Roles companyProfileRelation = roles.get();
        return ResponseEntity.ok(new AuthorizationResponse(companyProfileRelation.getRole()));
    }
}