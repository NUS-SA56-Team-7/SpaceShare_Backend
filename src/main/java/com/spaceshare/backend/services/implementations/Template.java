package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.repos.PropertyRepository;

public class Template {
	
	/*** Repositories ***/
	@Autowired
	PropertyRepository repoProperty;

	/*** Methods ***/
	public Boolean createProperty(Property property) {
		try {
			property.setCreatedAt(LocalDate.now());
			property.setUpdatedAt(LocalDate.now());
			
			repoProperty.save(property);
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public Property getPropertyById(Long id) {
		return repoProperty.findById(null)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public Boolean deleteStudent(Long id) {
		return repoProperty.findById(id)
				.map(existingProperty -> {
					repoProperty.delete(existingProperty);
					return true;
				})
				.get();
	}
}
