package com.spaceshare.backend.services;

import com.spaceshare.backend.models.Facility;

import java.util.List;

public interface FacilityService {

    List<Facility> getAllFacilities();

    Facility getFacilityById(Long id);

    Facility addFacility(Facility facility);

    Facility updateFacility(Facility facility);

    boolean deleteFacility(Long id);

    boolean isFacilityNameExists(String facilityName);

}
