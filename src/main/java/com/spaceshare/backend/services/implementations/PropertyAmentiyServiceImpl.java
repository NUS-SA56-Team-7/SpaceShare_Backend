package com.spaceshare.backend.services.implementations;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Amenity;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.PropertyAmenity;
import com.spaceshare.backend.repos.AmenityRepository;
import com.spaceshare.backend.repos.PropertyAmenityRepository;
import com.spaceshare.backend.services.PropertyAmenityService;

@Service
public class PropertyAmentiyServiceImpl implements PropertyAmenityService {
    
	/*** Repositories ***/
	@Autowired
	PropertyAmenityRepository repoPropertyAmenity;
	
	@Autowired
	AmenityRepository repoAmenity;
	
	/*** Methods ***/
	@Transactional
	public List<PropertyAmenity> createPropertyAmenities(Property property, List<Long> amenitiyIDs) {
		
		List<PropertyAmenity> propertyAmenities = new ArrayList<PropertyAmenity>();
		
		for (Long id: amenitiyIDs) {
			Amenity amenity = repoAmenity.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException());
			
			PropertyAmenity propertyAmenity = new PropertyAmenity();
			propertyAmenity.setProperty(property);
			propertyAmenity.setAmenity(amenity);
			
			propertyAmenities.add(repoPropertyAmenity.save(propertyAmenity));
		}
		
		return propertyAmenities;
	}
	
	@Transactional
	public Boolean deletePropertyAmenities(Long propertyId) {
		List<PropertyAmenity> propertyAmenities = repoPropertyAmenity.findByPropertyId(propertyId);
		for (PropertyAmenity amenity: propertyAmenities) {
			repoPropertyAmenity.deleteById(amenity.getId());
		}
		
		return true;
	}
}
