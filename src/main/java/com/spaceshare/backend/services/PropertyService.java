package com.spaceshare.backend.services;

import java.util.List;

import com.spaceshare.backend.models.Property;

public interface PropertyService {
    
	Boolean createProperty(Property property);
	
	Property getPropertyById(Long id);
	
	List<Property> getAllProperties();
}
