package com.spaceshare.backend.services;

import com.spaceshare.backend.models.Amenity;

import java.util.List;

public interface AmenityService {

        List<Amenity> getAllAmenities();

        public Amenity getAmenityById(Long id);

        boolean addAmenity(Amenity amenity);

        boolean updateAmenity(Long id, Amenity updatedAmenity);

        boolean deleteAmenity(Long id);

        boolean isAmenityNameExists(String amenityName);

}
