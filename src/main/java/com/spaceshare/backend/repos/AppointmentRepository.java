package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
}
