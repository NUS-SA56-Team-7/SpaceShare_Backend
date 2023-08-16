package com.spaceshare.backend.services;

import com.spaceshare.backend.models.Amenity;

import java.util.List;

public interface AmenityService {

    List<Amenity> getAllAmenities();

    Amenity getAmenityById(Long id);

    Amenity addAmenity(Amenity amenity);

    // Amenity updateAmenity(Long id, Amenity amenity);

    Boolean deleteAmenity(Long id);

    Boolean isAmenityNameExists(String amenityName);
}
