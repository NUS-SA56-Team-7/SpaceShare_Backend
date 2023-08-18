package com.spaceshare.backend.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.spaceshare.backend.dtos.TenantDTO;
import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.RecentSearch;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.projections.RecentSearchProjection;
import com.spaceshare.backend.services.RecentSearchService;
import com.spaceshare.backend.models.enums.Status;
import com.spaceshare.backend.services.RenterService;
import com.spaceshare.backend.services.TenantService;

@RestController
@RequestMapping("api/tenant")
public class TenantController {

	/*** Services ***/
	@Autowired
	TenantService svcTenant;

	@Autowired
	RecentSearchService svcRecentSearch;

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
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("")
	public ResponseEntity<List<Tenant>> getAllTenants() {
		List<Tenant> tenants = svcTenant.getAllTenants();
		if (tenants.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Tenant>>(tenants, HttpStatus.OK);
	}

	@GetMapping("/{id}/favourites/id")
	public ResponseEntity<?> getAllFavouriteIDs(
			@PathVariable UUID id) {
		try {
			List<Long> favouritePropertyIDs = svcTenant.getTenantById(id).getFavourites()
					.stream()
					.map((favourite) -> {
						return favourite.getProperty().getId();
					}).toList();

			return new ResponseEntity<>(favouritePropertyIDs, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{id}/recent/create")
	public ResponseEntity<?> postCreateRecentSearch(
			@PathVariable UUID id,
			@RequestBody Map<String, String> reqBody) {
		try {
			Boolean success = svcRecentSearch.createRecentSearch(
					id, Long.parseLong(reqBody.get("propertyId")));
			if (success) {
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}/recent/")
	public ResponseEntity<?> getAllRecentSearchesByTenant (
			@PathVariable UUID id) {
		try {
			List<RecentSearchProjection> recentSearches =
					svcRecentSearch.getRecentSearchesByTenantId(id);
			return new ResponseEntity<>(recentSearches, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
	@PostMapping("/register")
	public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
		try {

			Boolean success = svcTenant.saveTenant(tenant);
			if (success) {
				return new ResponseEntity<Tenant>(HttpStatus.CREATED);
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
	public ResponseEntity<?> getTenantById(@PathVariable UUID id) {
		Tenant tenant = svcTenant.getTenantById(id);

		if (tenant != null) {
			return new ResponseEntity<>(tenant, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteTenant(@PathVariable("id") UUID id) {

		try {
			Boolean result = svcTenant.deleteTenant(id);

			if (result)
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

	@PutMapping("/update/{id}")
	public ResponseEntity<Tenant> updateTenant(@PathVariable("id") UUID id, @RequestBody Tenant inTenant) {

		try {
			Tenant updatedTenant = svcTenant.updateTenant(id, inTenant);
			if (updatedTenant != null)
				return new ResponseEntity<Tenant>(updatedTenant, HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/export/list")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=tenants_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		List<Tenant> listTenants = svcTenant.getAllTenants();
		Map<Status, List<Tenant>> statusToTenants = listTenants.stream().
				sorted(Comparator.comparing(Tenant::getFirstName)).collect(Collectors.groupingBy(Tenant::getStatus));

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "First Name", "Last Name", "Identification Number", "Date Of Birth",
				"Address", "Email", "Phone",  "Status" };
		String[] nameMapping = { "firstName", "lastName", "identificationNumber", "dateOfBirth",
				"address", "email", "phone",  "status" };

		// for (Tenant tenant : listTenants) {
		// 	csvWriter.write(tenant, nameMapping);
		// }

		statusToTenants.forEach((status, tenants) -> {
			try {
				csvWriter.writeComment(status.toString());
				csvWriter.writeHeader(csvHeader);
			} catch (IOException e) {
				e.printStackTrace();
			}
			tenants.forEach(tenant -> 
				{
					try {
						csvWriter.write(tenant, nameMapping);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			);
		});

		csvWriter.close();
	}
}
