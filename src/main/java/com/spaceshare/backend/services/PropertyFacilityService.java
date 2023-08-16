package com.spaceshare.backend.services;

import java.util.List;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.PropertyFacility;

public interface PropertyFacilityService {
    
	List<PropertyFacility> createPropertyFacilities(Property property, List<Long> facilityIDs);
}
