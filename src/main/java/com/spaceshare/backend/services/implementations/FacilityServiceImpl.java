package com.spaceshare.backend.services.implementations;

import com.spaceshare.backend.repos.FacilityRepository;
import org.springframework.stereotype.Service;
import com.spaceshare.backend.models.Facility;

import java.util.List;

import com.spaceshare.backend.services.FacilityService;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityServiceImpl(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    @Override
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    @Override
    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id).orElse(null);
    }

    @Override
    public Facility addFacility(Facility facility) {
        if (!isFacilityNameExists(facility.getFacilityName())) {
            return facilityRepository.save(facility);
        }
        return null;
    }

    @Override
    public Facility updateFacility(Facility updatedFacility) {
        Facility existingFacility = facilityRepository.findById(updatedFacility.getId()).orElse(null);
        if (existingFacility != null) {
            if (!isFacilityNameExists(updatedFacility.getFacilityName())) {
                existingFacility.setFacilityName(updatedFacility.getFacilityName());
                return facilityRepository.save(existingFacility);
            } else {
                throw new RuntimeException("Name is already exist");
            }
        }
        return null;
    }

    @Override
    public boolean deleteFacility(Long id) {
        if (facilityRepository.existsById(id)) {
            facilityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFacilityNameExists(String facilityName) {
        return facilityRepository.existsByFacilityName(facilityName);
    }

}
