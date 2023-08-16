package com.spaceshare.backend.services;

import com.spaceshare.backend.models.Facility;

import java.util.List;

public interface FacilityService {

    List<Facility> getAllFacilities();

    Facility getFacilityById(Long id);

    Facility addFacility(Facility facility);

    // Facility updateFacility(Long id, Facility facility);

    Boolean deleteFacility(Long id);

    Boolean isFacilityNameExists(String facilityName);

}
