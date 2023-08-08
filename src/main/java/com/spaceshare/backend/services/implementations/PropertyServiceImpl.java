package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.services.PropertyImageService;
import com.spaceshare.backend.services.PropertyService;

@Service
public class PropertyServiceImpl implements PropertyService {
    
	/*** Repositories ***/
	@Autowired
	PropertyRepository repoProperty;
	
	/*** Services ***/
	@Autowired
	PropertyImageService svcPropertyImage;
	
	/*** Methods ***/
	@Transactional
	public Boolean createProperty(Property property) {
		try {
			property.setCreatedAt(LocalDate.now());
			property.setUpdatedAt(LocalDate.now());
			
			Property savedProperty = repoProperty.save(property);			
			System.out.println(property.getPropertyImageURLs());
			svcPropertyImage.createPropertyImages(savedProperty, property.getPropertyImageURLs());

			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public Property getPropertyById(Long id) {
		return repoProperty.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public List<Property> getAllProperties() {
		return repoProperty.findAll();
	}
}
