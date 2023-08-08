package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    boolean existsByFacilityName(String facilityName);

}
