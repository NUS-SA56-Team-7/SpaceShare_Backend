package com.spaceshare.backend.services;

import com.spaceshare.backend.models.Facility;

import java.util.List;

public interface FacilityService {

    List<Facility> getAllFacilities();

    Facility getFacilityById(Long id);

    Boolean addFacility(Facility facility);

    Boolean updateFacility(Long id, Facility facility);

    Boolean deleteFacility(Long id);

    Boolean isFacilityNameExists(String facilityName);

}
