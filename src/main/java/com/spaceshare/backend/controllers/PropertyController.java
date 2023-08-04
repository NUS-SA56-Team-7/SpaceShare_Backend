package com.spaceshare.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.services.PropertyService;

@RestController
@RequestMapping("/api/property")
public class PropertyController {
    
	/*** Services ***/
	@Autowired
	PropertyService svcProperty;
	
	/*** Methods ***/
	@PostMapping("/create")
	public ResponseEntity<?> postCreateProperty(Property property) {
		Boolean success = svcProperty.createProperty(property);
		
		if (success) {
			return new ResponseEntity<>(property, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
