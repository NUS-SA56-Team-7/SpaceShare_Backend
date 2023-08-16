package com.spaceshare.backend.services.implementations;

import com.spaceshare.backend.repos.FacilityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.DuplicateResourceException;
import com.spaceshare.backend.exceptions.InternalServerErrorException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Facility;

import java.util.List;

import com.spaceshare.backend.services.FacilityService;

@Service
public class FacilityServiceImpl implements FacilityService {

    /*** Repositories ***/
    @Autowired
    FacilityRepository facilityRepository;

    /*** Methods ***/
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    public Facility addFacility(Facility facility) {
        if (isFacilityNameExists(facility.getFacilityName()))
            throw new DuplicateResourceException();
        try {
            return facilityRepository.save(facility);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    // public Facility updateFacility(Long id, Facility updatedFacility) {
    // Facility existingFacility =
    // facilityRepository.findById(updatedFacility.getId()).orElse(null);
    // if (existingFacility != null) {
    // if (!isFacilityNameExists(updatedFacility.getFacilityName())) {
    // existingFacility.setFacilityName(updatedFacility.getFacilityName());
    // facilityRepository.save(existingFacility);
    // return true;
    // } else {
    // throw new RuntimeException("Name is already exist");
    // }
    // }
    // return false;
    // }

    public Boolean deleteFacility(Long id) {
        try {
            if (facilityRepository.existsById(id)) {
                facilityRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Boolean isFacilityNameExists(String facilityName) {
        return facilityRepository.existsByFacilityName(facilityName);
    }

}
