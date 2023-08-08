package com.spaceshare.backend.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.services.RenterService;
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
			List<Property> favouriteProperties = svcTenant.getTenantById(id).getFavourites()
					.stream()
					.map((favourite) -> {
						return favourite.getProperty();
					}).toList();
			return new ResponseEntity<>(favouriteProperties, HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);			
			}
	}

	// @PostMapping("/register")
	// public ResponseEntity<?> postRegisterRenter(@RequestBody Renter renter) {
	// Boolean success = svcRenter.createRenter(renter);

	// if (success) {
	// return new ResponseEntity<>(renter, HttpStatus.OK);
	// }
	// else {
	// return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
	// }
	// }

	public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
		try {

			if (svcTenant.getTenantByEmail(tenant.getEmail()) != null) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}

			Tenant returnTenant = svcTenant.saveTenant(tenant);

			return new ResponseEntity<Tenant>(returnTenant, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Tenant> getTenantById(@PathVariable("id") UUID id) {

		Tenant tenant = svcTenant.getTenantById(id);

		if (tenant != null) {

			return new ResponseEntity<Tenant>(tenant, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteTenant(@PathVariable("id") UUID id) {
		try {
			Tenant tenant = svcTenant.getTenantById(id);
			svcTenant.deleteTenant(tenant);

			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Tenant> editTenant(@PathVariable("id") UUID id, @RequestBody Tenant inTenant) {

		Tenant renter = svcTenant.getTenantById(id);

		if (renter != null) {
			Tenant r = renter;

            r.setAddress(inTenant.getAddress());
			r.setCreatedAt(inTenant.getCreatedAt());
			r.setDateOfBirth(inTenant.getDateOfBirth());
			r.setEmail(inTenant.getEmail());
			r.setFirstName(inTenant.getFirstName());
			r.setIdentificationNumber(inTenant.getIdentificationNumber());
			r.setLastName(inTenant.getLastName());
			r.setPassword(inTenant.getPassword());
			r.setPhone(inTenant.getPhone());
			//r.setProfileImageUrl(inTenant.getProfileImageUrl());
			r.setStatus(inTenant.getStatus());
			r.setUpdatedAt(inTenant.getUpdatedAt());

			Tenant updatedTenant = svcTenant.updateTenant(r);

			return new ResponseEntity<Tenant>(updatedTenant, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// issuing get request to this route produce csv file
	// @GetMapping("/export/list")
	// public void exportToCSV(HttpServletResponse response) throws IOException {
	// response.setContentType("text/csv");
	// DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	// String currentDateTime = dateFormatter.format(new Date());

	// String headerKey = "Content-Disposition";
	// String headerValue = "attachment; filename=tenants_" + currentDateTime +
	// ".csv";
	// response.setHeader(headerKey, headerValue);

	// List<Tenant> listTenants = svcTenant.findAllTenants();

	// ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
	// CsvPreference.STANDARD_PREFERENCE);
	// String[] csvHeader = { "First Name", "Last Name", "Identification Number",
	// "Address", "Phone", "Date Of Birth" };
	// String[] nameMapping = { "firstName", "lastName", "identificationNumber",
	// "address", "phone", "dateOfBirth" };

	// csvWriter.writeHeader(csvHeader);

	// for (Tenant tenant : listTenants) {
	// csvWriter.write(tenant, nameMapping);
	// }

	// csvWriter.close();
	// }
}
