package com.spaceshare.backend.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.PropertyAmenity;

@Repository
public interface PropertyAmenityRepository extends JpaRepository<PropertyAmenity, UUID> {
    
}
