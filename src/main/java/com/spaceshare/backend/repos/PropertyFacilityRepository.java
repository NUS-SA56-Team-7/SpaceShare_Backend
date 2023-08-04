package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.PropertyFacility;

@Repository
public interface PropertyFacilityRepository extends JpaRepository<PropertyFacility, Long> {
    
}
