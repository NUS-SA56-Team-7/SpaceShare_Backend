package com.spaceshare.backend.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
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
	public Boolean createPropertyImages(Property property, List<String> propertyImageURLs) {
		try {
			for (String imageURL: propertyImageURLs) {
				PropertyImage propertyImage = new PropertyImage();
				propertyImage.setImageUrl(imageURL);
				propertyImage.setProperty(property);
				repoPropertyImage.save(propertyImage);
			}
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}
