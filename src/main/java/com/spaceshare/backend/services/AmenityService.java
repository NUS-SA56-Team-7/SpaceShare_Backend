package com.spaceshare.backend.services;

import com.spaceshare.backend.models.Amenity;

import java.util.List;

public interface AmenityService {

        List<Amenity> getAllAmenities();

        public Amenity getAmenityById(Long id);

        Amenity addAmenity(Amenity amenity);

        Amenity updateAmenity(Amenity amenity);

        boolean deleteAmenity(Long id);

        boolean isAmenityNameExists(String amenityName);

}
