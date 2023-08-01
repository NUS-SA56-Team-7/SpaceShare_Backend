package com.spaceshare.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.services.RenterService;

@RestController
@RequestMapping("/api/renter")
public class RenterController {
	
	@Autowired
	RenterService svcRenter;

	@PostMapping("/register")
	public ResponseEntity<?> postRegisterRenter(@RequestBody Renter renter) {
		Boolean success = svcRenter.createRenter(renter);
		
		if (success) {
			return new ResponseEntity<>(renter, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
		}
	}
}
