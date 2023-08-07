package com.spaceshare.backend.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Favourite;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.services.FavouriteService;

@RestController
@RequestMapping("/api/favourite")
public class FavouriteController {
    
	/*** Service Imports ***/
	@Autowired
	FavouriteService svcFavourite;
	
	/*** API Methods ***/
	@PostMapping("/create")
	public ResponseEntity<?> postCreateFavourite(
			@RequestBody Map<String, String> reqBody) {
		try {
			Boolean success = svcFavourite.createFavourite(
					UUID.fromString((String)reqBody.get("tenantId")),
					Long.parseLong(reqBody.get("propertyId")));
			
			if (success) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteFavourite(
			@RequestParam UUID tenantId,
			@RequestParam Long propertyId) {
		try {
			if (svcFavourite.deleteFavourite(propertyId)) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}