package com.spaceshare.backend.services;

import com.spaceshare.backend.models.Facility;

import java.util.List;

public interface FacilityService {

    List<Facility> getAllFacilities();

    Facility getFacilityById(Long id);

    boolean addFacility(Facility facility);

    boolean updateFacility(Long id, Facility facility);

    boolean deleteFacility(Long id);

    boolean isFacilityNameExists(String facilityName);

}
