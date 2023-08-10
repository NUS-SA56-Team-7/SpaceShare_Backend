package com.spaceshare.backend.controllers;

import java.util.List;
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

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Account;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.services.RenterService;
import com.spaceshare.backend.services.TenantService;

@RestController
@RequestMapping("/api/renter")
public class RenterController {
	
	@Autowired
	RenterService svcRenter;

	// @PostMapping("/register")
	// public ResponseEntity<?> postRegisterRenter(@RequestBody Renter renter) {
	// Boolean success = svcRenter.createRenter(renter);

	// if (success) {
	// return new ResponseEntity<>(renter, HttpStatus.OK);
	// } else {
	// return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
	// }
	// }

	@GetMapping("/all")
	public ResponseEntity<List<Renter>> getAllRenters() {

		try {
			List<Renter> renters = svcRenter.getAllRenters();
			if (renters.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<List<Renter>>(renters, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Renter> getRenterById(@PathVariable("id") UUID id) {

		try {
			Renter renter = svcRenter.findRenterById(id);

			if (renter != null) {
				return new ResponseEntity<Renter>(renter, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Renter> createRenter(@RequestBody Renter renter) {
		try {
			Boolean success = svcRenter.createRenter(renter);

			if (success) {
				return new ResponseEntity<Renter>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Renter> updateRenter(@PathVariable("id") UUID id, @RequestBody Renter inRenter) {

		try {
			Renter renter = svcRenter.updateRenter(id, inRenter);

			if (renter != null)
				return new ResponseEntity<Renter>(renter, HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteRenter(@PathVariable("id") UUID id) {

		try {
			Boolean success = svcRenter.deleteRenter(id);

			if (success)
				return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
}
