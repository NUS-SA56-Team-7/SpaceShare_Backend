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
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.services.PropertyService;
import com.spaceshare.backend.services.RenterService;
import com.spaceshare.backend.services.TenantService;

@RestController
@RequestMapping("/api/renter")
public class RenterController {
	/*** Services ***/
	@Autowired
	RenterService svcRenter;

	@Autowired
	PropertyService svcProperty;

	/*** API Methods ***/
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

	/*** API Methods ***/
	@PostMapping("/{id}/property/create")
	public ResponseEntity<?> postCreateProperty(
			@PathVariable("id") UUID renterId,
			@RequestBody Property property) {
		try {
			Boolean success = svcProperty.createRenterProperty(renterId, property);
			if (success) {
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}/property/update/{propertyId}")
	public ResponseEntity<?> putUpdateProperty(
			@PathVariable UUID id,
			@PathVariable Long propertyId,
			@RequestBody Property property) {
		try {
			Boolean success = svcProperty.updateRenterProperty(id, propertyId, property);
			if (success) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{id}/property/delete/{propertyId}")
	public ResponseEntity<?> deleteProperty(
			@PathVariable UUID id,
			@PathVariable Long propertyId) {
		try {
			Boolean success = svcProperty.deleteRenterProperty(id, propertyId);
			if (success) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}/properties")
	public ResponseEntity<?> getRenterProperties(
			@PathVariable UUID id) {
		try {
			return new ResponseEntity<>(svcProperty.getPropertiesByRenterId(id), HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRenterById(@PathVariable UUID id) {
		try {
			Renter renter = svcRenter.getRenterById(id);
			return new ResponseEntity<Renter>(renter, HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateRenter(
			@PathVariable("id") UUID id,
			@RequestBody Renter inRenter) {
		try {
			Renter renter = svcRenter.updateRenter(id, inRenter);
			return new ResponseEntity<Renter>(renter, HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
}
