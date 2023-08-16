package com.spaceshare.backend.services;

import java.util.List;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.PropertyAmenity;

public interface PropertyAmenityService {
    
	List<PropertyAmenity> createPropertyAmenities(Property property, List<Long> amenitiyIDs);
}
