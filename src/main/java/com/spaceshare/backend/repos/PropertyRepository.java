package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    
}
