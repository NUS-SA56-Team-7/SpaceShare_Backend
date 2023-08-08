package com.spaceshare.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.services.PropertyService;

public class Template {

	/*** Services ***/
	@Autowired
	PropertyService svcProperty;
	
	/*** Methods ***/
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
