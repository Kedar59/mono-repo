package com.apiGateway.repositories;

import com.apiGateway.entities.Role;
import com.apiGateway.entities.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RolesRepository extends MongoRepository<Roles, String> {
    Optional<Roles> findByProfileIdAndCompanyId(String profileId, String companyId);
    boolean existsByProfileIdAndCompanyIdAndRole(String profileId, String companyId, Role role);
}
