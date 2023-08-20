package com.spaceshare.backend.services;

import java.util.List;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.PropertyImage;

public interface PropertyImageService {
 
	List<PropertyImage> createPropertyImages(Property property, List<String> propertyImageURLs);
	
	List<PropertyImage> updatePropertyImages(Property property, List<String> propertyImageURLs);
	
	Boolean deletePropertyImages(Long propertyId);
}
