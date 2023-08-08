package com.spaceshare.backend.services;

import java.util.List;

import com.spaceshare.backend.models.Property;

public interface PropertyImageService {
 
	Boolean createPropertyImages(Property property, List<String> propertyImageURLs);
}
