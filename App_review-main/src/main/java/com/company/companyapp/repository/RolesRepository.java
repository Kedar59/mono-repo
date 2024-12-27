package com.company.companyapp.repository;

import com.company.companyapp.model.Role;
import com.company.companyapp.model.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RolesRepository extends MongoRepository<Roles, String> {
    Optional<Roles> findByProfileIdAndCompanyId(String profileId, String companyId);
    boolean existsByProfileIdAndCompanyIdAndRole(String userId, String companyId, Role role);
    void deleteByCompanyId(String companyId);
    void deleteByProfileIdAndCompanyId(String profileId,String companyId);
    List<String> findProfileIdsByCompanyId(String companyId);
    List<Roles> findRolesByCompanyId(String companyId);
}
