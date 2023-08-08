package com.spaceshare.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Favourite;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.services.TenantService;

@RestController
@RequestMapping("api/tenant")
public class TenantController {
	
	/*** Services ***/
	@Autowired
	TenantService svcTenant;
    
	/*** API Methods ***/
	@GetMapping("/{id}/favourites")
	public ResponseEntity<?> getAllFavourites(
			@PathVariable UUID id) {
		try {
			List<Property> favouriteProperties =
					svcTenant.getTenantById(id).getFavourites()
					.stream()
					.map((favourite) -> {
						return favourite.getProperty();
					}).toList();
			return new ResponseEntity<>(favouriteProperties, HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
