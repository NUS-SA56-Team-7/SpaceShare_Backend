package com.spaceshare.backend.services;

import com.spaceshare.backend.models.Amenity;

import java.util.List;

public interface AmenityService {

	List<Amenity> getAllAmenities();

	public Amenity getAmenityById(Long id);

	Boolean addAmenity(Amenity amenity);

	Boolean updateAmenity(Long id, Amenity updatedAmenity);

	Boolean deleteAmenity(Long id);

	Boolean isAmenityNameExists(String amenityName);

}
