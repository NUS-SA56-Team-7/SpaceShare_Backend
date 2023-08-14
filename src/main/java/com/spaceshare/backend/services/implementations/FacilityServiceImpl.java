package com.spaceshare.backend.services.implementations;

import com.spaceshare.backend.repos.FacilityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spaceshare.backend.models.Facility;

import java.util.List;

import com.spaceshare.backend.services.FacilityService;

@Service
public class FacilityServiceImpl implements FacilityService {

    /*** Repositories ***/
    @Autowired
    FacilityRepository facilityRepository;

    /*** Methods ***/
    @Override
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    @Override
    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean addFacility(Facility facility) {
        if (!isFacilityNameExists(facility.getFacilityName())) {
            facilityRepository.save(facility);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateFacility(Long id, Facility updatedFacility) {
        Facility existingFacility = facilityRepository.findById(id).orElse(null);
        if (existingFacility != null) {
            if (!isFacilityNameExists(updatedFacility.getFacilityName())) {
                existingFacility.setFacilityName(updatedFacility.getFacilityName());
                facilityRepository.save(existingFacility);
                return true;
            } else {
                throw new RuntimeException("Name is already exist");
            }
        }
        return false;
    }

    @Override
    public Boolean deleteFacility(Long id) {
        if (facilityRepository.existsById(id)) {
            facilityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean isFacilityNameExists(String facilityName) {
        return facilityRepository.existsByFacilityName(facilityName);
    }

}
