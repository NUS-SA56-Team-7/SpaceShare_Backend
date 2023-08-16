package com.spaceshare.backend.services.implementations;

import com.spaceshare.backend.repos.AmenityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.DuplicateResourceException;
import com.spaceshare.backend.exceptions.InternalServerErrorException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Amenity;

import java.util.List;
import com.spaceshare.backend.services.AmenityService;

@Service
public class AmenityServiceImpl implements AmenityService {

    /*** Repositories ***/
    @Autowired
    AmenityRepository amenityRepository;

    /*** Methods ***/
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    public Amenity getAmenityById(Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    public Amenity addAmenity(Amenity amenity) {
        if (isAmenityNameExists(amenity.getAmenityName()))
            throw new DuplicateResourceException();
        try {
            return amenityRepository.save(amenity);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    // public Amenity updateAmenity(Long id, Amenity updatedAmenity) {
    // Amenity existingAmenity =
    // amenityRepository.findById(updatedAmenity.getId()).orElse(null);
    // if (existingAmenity != null) {
    // if (!isAmenityNameExists(updatedAmenity.getAmenityName())) {
    // existingAmenity.setAmenityName(updatedAmenity.getAmenityName());
    // amenityRepository.save(existingAmenity);
    // return true;
    // } else {
    // throw new RuntimeException("Name is already exist");
    // }
    // }
    // return false;
    // }

    public Boolean deleteAmenity(Long id) {
        try {
            if (amenityRepository.existsById(id)) {
                amenityRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Boolean isAmenityNameExists(String amenityName) {
        return amenityRepository.existsByAmenityName(amenityName);
    }
}
