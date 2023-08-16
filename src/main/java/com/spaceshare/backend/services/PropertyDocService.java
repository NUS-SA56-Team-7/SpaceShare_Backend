package com.spaceshare.backend.services;

import java.util.List;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.PropertyDoc;

public interface PropertyDocService {
    
	List<PropertyDoc> createPropertyDocs(Property property, List<String> propertyDocURLs);
}
