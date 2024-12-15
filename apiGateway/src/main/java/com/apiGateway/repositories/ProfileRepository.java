package com.apiGateway.repositories;

import com.apiGateway.entities.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile,String> {
    Optional<Profile> findByPhoneNumber(String phoneNumber);
    Optional<Profile> findByEmail(String email);

    @Query(value = "{ 'phoneNumber' : ?0 , 'countryCode' : ?1 }")
    Optional<Profile> findByCallerId(String phoneNumber,String countryCode);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Profile> findByNameContaining(String name);

}
