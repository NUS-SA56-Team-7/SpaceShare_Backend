package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Amenity;

@Repository
public interface AppointmentRepository extends JpaRepository<Amenity, Long> {
    
}
