package com.spaceshare.backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Account;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.services.RenterService;
import com.spaceshare.backend.services.TenantService;

@RestController
@RequestMapping("/api/renter")
public class RenterController {
	
	@Autowired
	RenterService svcRenter;

	// @PostMapping("/register")
	// public ResponseEntity<?> postRegisterRenter(@RequestBody Renter renter) {
	// 	Boolean success = svcRenter.createRenter(renter);
		
	// 	if (success) {
	// 		return new ResponseEntity<>(renter, HttpStatus.OK);
	// 	}
	// 	else {
	// 		return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
	// 	}
	// }

	@PostMapping("/register")
	public ResponseEntity<Renter> createRenter(@RequestBody Renter renter) {
		try {
			if (svcRenter.getRenterByEmail(renter.getEmail()) != null) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}

			Renter returnRenter = svcRenter.saveRenter(renter);

			return new ResponseEntity<Renter>(returnRenter, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Renter> getRenterById(@PathVariable("id") UUID id) {
		
		Renter renter = svcRenter.findRenterById(id);

		if (renter != null) {

			return new ResponseEntity<Renter>(renter, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteRenter(@PathVariable("id") UUID id) {
		try {
			Renter renter = svcRenter.findRenterById(id);
			svcRenter.deleteRenter(renter);
			
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Renter> editRenter(@PathVariable("id") UUID id, @RequestBody Renter inRenter) {

		Renter renter = svcRenter.findRenterById(id);

		if (renter != null) {
			Renter r = renter;

            r.setAddress(inRenter.getAddress());
			r.setCreatedAt(inRenter.getCreatedAt());
			r.setDateOfBirth(inRenter.getDateOfBirth());
			r.setEmail(inRenter.getEmail());
			r.setFirstName(inRenter.getFirstName());
			r.setIdentificationNumber(inRenter.getIdentificationNumber());
			r.setLastName(inRenter.getLastName());
			r.setPassword(inRenter.getPassword());
			r.setPhone(inRenter.getPhone());
			//r.setProfileImageUrl(inRenter.getProfileImageUrl());
			r.setStatus(inRenter.getStatus());
			r.setUpdatedAt(inRenter.getUpdatedAt());

			Renter updatedRenter = svcRenter.updateRenter(r);

			return new ResponseEntity<Renter>(updatedRenter, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
