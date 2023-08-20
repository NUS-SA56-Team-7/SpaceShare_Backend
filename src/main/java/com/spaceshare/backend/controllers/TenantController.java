package com.spaceshare.backend.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.DuplicateResourceException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.services.PropertyService;
import com.spaceshare.backend.services.RecentSearchService;
import com.spaceshare.backend.models.enums.Status;
import com.spaceshare.backend.services.TenantService;

@RestController
@RequestMapping("api/tenant")
public class TenantController {

	/*** Services ***/
	@Autowired
	TenantService svcTenant;
	
	@Autowired
	PropertyService svcProperty;

	@Autowired
	RecentSearchService svcRecentSearch;

	/*** API Methods ***/
	@PostMapping("/{id}/property/create")
	public ResponseEntity<?> postCreateProperty(
			@PathVariable("id") UUID tenantId,
			@RequestBody Property property) {
		try {
			Boolean success = svcProperty.createTenantProperty(tenantId, property);
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
			Boolean success = svcProperty.updateTenantProperty(id, propertyId, property);
			if (success) {
				return new ResponseEntity<>(HttpStatus.OK);
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
	
	@DeleteMapping("/{id}/property/delete/{propertyId}")
	public ResponseEntity<?> deleteProperty(
			@PathVariable UUID id,
			@PathVariable Long propertyId) {
		try {
			Boolean success = svcProperty.deleteTenantProperty(id, propertyId);
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
	public ResponseEntity<?> getTenantProperties(
			@PathVariable UUID id) {
		try {
			return new ResponseEntity<>(svcProperty.getPropertiesByTenantId(id), HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}/recommended")
	public ResponseEntity<?> getTenantRecommendedProperties(
			@PathVariable("id") UUID userId) {
		try {
			HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	        
	        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
	        map.add("id", "c1272223-4afa-4730-ae2f-8c58f620c132");
	        
	        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
	        
			RestTemplate template = new RestTemplate();
			String apiML = "http://localhost:8080/api/analytics/classify";
			ResponseEntity<String> responseEntity =
					template.postForEntity(apiML, requestEntity, String.class);
			
			String responseBody = responseEntity.getBody();
			
			List<Object> properties = new ArrayList<>();
			
			ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            JsonNode jsonNode = objectMapper.readTree(responseBody);
	            
	            List<Long> values = new ArrayList<>();

	            for (JsonNode node : jsonNode) {
	                values.add(Long.parseLong(node.asText()));
	            }

	            for (Long value: values) {
	            	properties.add(svcProperty.getPropertyById(value));
	            }
	        } catch (Exception e) {
	            // Handle the parsing exception
	        }
	      
			return new ResponseEntity<>(properties, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
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
	
	@GetMapping("/{id}/recents")
	public ResponseEntity<?> getAllRecentSearchesByTenant (
			@PathVariable UUID id) {
		try {
			List<Property> recentProperties = svcTenant.getTenantById(id).getRecents()
					.stream()
					.map((recent) -> {
						return recent.getProperty();
					}).toList();

			return new ResponseEntity<>(recentProperties, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
	public ResponseEntity<?> updateTenant(
			@PathVariable("id") UUID id,
			@RequestBody Tenant inTenant) {
		try {
			Tenant updatedTenant = svcTenant.updateTenant(id, inTenant);
			return new ResponseEntity<>(updatedTenant, HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch (DuplicateResourceException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
		catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
