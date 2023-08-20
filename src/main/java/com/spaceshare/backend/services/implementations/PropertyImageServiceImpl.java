package com.spaceshare.backend.services.implementations;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.PropertyImage;
import com.spaceshare.backend.repos.PropertyImageRepository;
import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.services.PropertyImageService;

@Service
public class PropertyImageServiceImpl implements PropertyImageService {
	
	/*** Repositories ***/
	@Autowired
	PropertyRepository repoProperty;
	
	@Autowired
	PropertyImageRepository repoPropertyImage;
	
	/*** Methods ***/
	@Transactional
	public List<PropertyImage> createPropertyImages(Property property, List<String> propertyImageURLs) {
		
		List<PropertyImage> propertyImages = new ArrayList<PropertyImage>();
		
		for (String imageURL: propertyImageURLs) {
			PropertyImage propertyImage = new PropertyImage();
			propertyImage.setImageUrl(imageURL);
			propertyImage.setProperty(property);
			
			propertyImages.add(repoPropertyImage.save(propertyImage));
		}

		return propertyImages;
	}
	
	@Transactional
	public List<PropertyImage> updatePropertyImages(Property property, List<String> propertyImageURLs) {
		
		List<PropertyImage> propertyImages = new ArrayList<PropertyImage>();
		
		for (String imageURL: propertyImageURLs) {
			PropertyImage propertyImage = new PropertyImage();
			propertyImage.setImageUrl(imageURL);
			propertyImage.setProperty(property);
			
			propertyImages.add(repoPropertyImage.save(propertyImage));
		}

		return propertyImages;
	}
	
	@Transactional
	public Boolean deletePropertyImages(Long propertyId) {
		List<PropertyImage> propertyImages = repoPropertyImage.findByPropertyId(propertyId);
		for (PropertyImage image: propertyImages) {
			repoPropertyImage.deleteById(image.getId());
		}
		
		return true;
	}
}
