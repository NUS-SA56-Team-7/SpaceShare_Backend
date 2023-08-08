package com.spaceshare.backend.services.implementations;

import com.spaceshare.backend.repos.AmenityRepository;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.models.Amenity;

import java.util.List;
import com.spaceshare.backend.services.AmenityService;

@Service
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityServiceImpl(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Override
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @Override
    public Amenity getAmenityById(Long id) {
        return amenityRepository.findById(id).orElse(null);
    }

    @Override
    public Amenity addAmenity(Amenity amenity) {
        if (!isAmenityNameExists(amenity.getAmenityName())) {
            return amenityRepository.save(amenity);
        }
        return null;
    }

    @Override
    public Amenity updateAmenity(Amenity updatedAmenity) {
        Amenity existingAmenity = amenityRepository.findById(updatedAmenity.getId()).orElse(null);
        if (existingAmenity != null) {
            if (!isAmenityNameExists(updatedAmenity.getAmenityName())) {
                existingAmenity.setAmenityName(updatedAmenity.getAmenityName());
                return amenityRepository.save(existingAmenity);
            } else {
                throw new RuntimeException("Name is already exist");
            }
        }
        return null;
    }

    @Override
    public boolean deleteAmenity(Long id) {
        if (amenityRepository.existsById(id)) {
            amenityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isAmenityNameExists(String amenityName) {
        return amenityRepository.existsByAmenityName(amenityName);
    }

}
