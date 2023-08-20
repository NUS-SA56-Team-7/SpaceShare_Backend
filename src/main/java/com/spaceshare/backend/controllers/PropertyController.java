package com.spaceshare.backend.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Amenity;
import com.spaceshare.backend.models.Comment;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.models.enums.PostType;
import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.TenantType;
import com.spaceshare.backend.models.enums.RoomType;
import com.spaceshare.backend.projections.CommentProjection;
import com.spaceshare.backend.projections.PropertyDetailProjection;
import com.spaceshare.backend.projections.PropertyProjection;
import com.spaceshare.backend.models.enums.Status;
import com.spaceshare.backend.services.CommentService;
import com.spaceshare.backend.services.PropertyService;

@RestController
@RequestMapping("/api/property")
public class PropertyController {

	/*** Services ***/
	@Autowired
	PropertyService svcProperty;

	@Autowired
	CommentService svcComment;

	/*** API Methods ***/
	@GetMapping("/{id}")
	public ResponseEntity<?> getPropertyById(@PathVariable Long id) {
		try {
			PropertyDetailProjection property = svcProperty.getPropertyById(id);
			return new ResponseEntity<>(property, HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getAllProperties(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int pageSize,
			@RequestParam(defaultValue = "title") String sortBy) {
		try {
			Page<PropertyProjection> propertyPage = svcProperty.getAllProperties(page, pageSize, sortBy);
			return new ResponseEntity<>(propertyPage, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> getSearchedProperties(
			@RequestParam(defaultValue = "ROOM_RENTAL") String postType,
			@RequestParam(required = false) String keyword,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int pageSize,
			@RequestParam(defaultValue = "title") String sortBy) {
		try {
			Page<PropertyProjection> propertyPage =
					svcProperty.getSearchedProperties(PostType.valueOf(postType), keyword, page, pageSize, sortBy);
			return new ResponseEntity<>(propertyPage, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Comments */
	@GetMapping("/{id}/comments")
	public ResponseEntity<?> getAllComments(@PathVariable Long id) {
		try {
			List<CommentProjection> comments = svcComment.getBaseComments(id);
			return new ResponseEntity<>(comments, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * show status count for each post type
	 */
	@GetMapping("/property-type-reports")
	public ResponseEntity<Map<String, Long>> getPropertyTypeReports() {
		Map<String, Long> propertyReports = new HashMap<>();
		try {
			// Getting Room Rentals Count by Pending and Approved Status
			propertyReports.put("pendingRoomRentalCount",
					svcProperty.countByPropertyTypeAndStatus(PostType.ROOM_RENTAL, ApproveStatus.PENDING));
			propertyReports.put("approvedRoomRentalCount",
					svcProperty.countByPropertyTypeAndStatus(PostType.ROOM_RENTAL, ApproveStatus.APPROVED));

			// Getting Roommate Findings Count by Pending and Approved Status
			propertyReports.put("pendingRoommateFindingCount",
					svcProperty.countByPropertyTypeAndStatus(PostType.ROOMMATE_FINDING, ApproveStatus.PENDING));
			propertyReports.put("approvedRoommateFindingCount",
					svcProperty.countByPropertyTypeAndStatus(PostType.ROOMMATE_FINDING, ApproveStatus.APPROVED));

			return new ResponseEntity<>(propertyReports, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * average Room Rentals by property type
	 */
	@GetMapping("/room-rental/property-type/percentages")
	public ResponseEntity<Map<PropertyType, Double>> getRoomRentalPercentagesByPropertyType() {
		Map<PropertyType, Double> percentages = new HashMap<>();
		try {
			percentages.put(PropertyType.HDB,
					svcProperty.calculatePropertyTypePercentages(PostType.ROOM_RENTAL, PropertyType.HDB));
			percentages.put(PropertyType.CONDOMINIUM,
					svcProperty.calculatePropertyTypePercentages(PostType.ROOM_RENTAL, PropertyType.CONDOMINIUM));
			percentages.put(PropertyType.LANDED,
					svcProperty.calculatePropertyTypePercentages(PostType.ROOM_RENTAL, PropertyType.LANDED));
			return new ResponseEntity<>(percentages, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * average Roommate Findings by property type
	 */
	@GetMapping("/roommate-finding/property-type/percentages")
	public ResponseEntity<Map<PropertyType, Double>> getRoommateFindingPercentagesByPropertyType() {
		Map<PropertyType, Double> percentages = new HashMap<>();
		try {
			percentages.put(PropertyType.HDB,
					svcProperty.calculatePropertyTypePercentages(PostType.ROOMMATE_FINDING, PropertyType.HDB));
			percentages.put(PropertyType.CONDOMINIUM,
					svcProperty.calculatePropertyTypePercentages(PostType.ROOMMATE_FINDING, PropertyType.CONDOMINIUM));
			percentages.put(PropertyType.LANDED,
					svcProperty.calculatePropertyTypePercentages(PostType.ROOMMATE_FINDING, PropertyType.LANDED));
			return new ResponseEntity<>(percentages, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * average Room Rentals by room type
	 */
	@GetMapping("/room-rental/room-type/percentages")
	public ResponseEntity<Map<RoomType, Double>> getRoomRentalPercentagesByRoomType() {
		Map<RoomType, Double> percentages = new HashMap<>();
		try {
			percentages.put(RoomType.SINGLE,
					svcProperty.calculateRoomTypePercentages(PostType.ROOM_RENTAL, RoomType.SINGLE));
			percentages.put(RoomType.COMMON,
					svcProperty.calculateRoomTypePercentages(PostType.ROOM_RENTAL, RoomType.COMMON));
			percentages.put(RoomType.MASTER,
					svcProperty.calculateRoomTypePercentages(PostType.ROOM_RENTAL, RoomType.MASTER));
			percentages.put(RoomType.WHOLE_UNIT,
					svcProperty.calculateRoomTypePercentages(PostType.ROOM_RENTAL, RoomType.WHOLE_UNIT));
			return new ResponseEntity<>(percentages, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * average Roommate Finding by room type
	 */
	@GetMapping("/roommate-finding/room-type/percentages")
	public ResponseEntity<Map<RoomType, Double>> getRoommateFindingPercentagesByRoomType() {
		Map<RoomType, Double> percentages = new HashMap<>();
		try {
			percentages.put(RoomType.SINGLE,
					svcProperty.calculateRoomTypePercentages(PostType.ROOMMATE_FINDING, RoomType.SINGLE));
			percentages.put(RoomType.COMMON,
					svcProperty.calculateRoomTypePercentages(PostType.ROOMMATE_FINDING, RoomType.COMMON));
			percentages.put(RoomType.MASTER,
					svcProperty.calculateRoomTypePercentages(PostType.ROOMMATE_FINDING, RoomType.MASTER));
			percentages.put(RoomType.WHOLE_UNIT,
					svcProperty.calculateRoomTypePercentages(PostType.ROOMMATE_FINDING, RoomType.WHOLE_UNIT));
			return new ResponseEntity<>(percentages, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * average Room Rentals by tenant type
	 */
	@GetMapping("/room-rental/roommate-type/percentages")
	public ResponseEntity<Map<TenantType, Double>> getRoomRentalPercentagesByRoomMateType() {
		Map<TenantType, Double> percentages = new HashMap<>();
		try {
			percentages.put(TenantType.MALE,
					svcProperty.calculateTenantTypePercentages(PostType.ROOM_RENTAL, TenantType.MALE));
			percentages.put(TenantType.FEMALE,
					svcProperty.calculateTenantTypePercentages(PostType.ROOM_RENTAL, TenantType.FEMALE));
			percentages.put(TenantType.COUPLE,
					svcProperty.calculateTenantTypePercentages(PostType.ROOM_RENTAL, TenantType.COUPLE));
			percentages.put(TenantType.FAMILY,
					svcProperty.calculateTenantTypePercentages(PostType.ROOM_RENTAL, TenantType.FAMILY));
			return new ResponseEntity<>(percentages, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * average Tenant Findings by tenant type
	 */
	@GetMapping("/roommate-finding/roommate-type/percentages")
	public ResponseEntity<Map<TenantType, Double>> getRoommateFindingPercentagesByRoomMateType() {
		Map<TenantType, Double> percentages = new HashMap<>();
		try {
			percentages.put(TenantType.MALE,
					svcProperty.calculateTenantTypePercentages(PostType.ROOM_RENTAL, TenantType.MALE));
			percentages.put(TenantType.FEMALE,
					svcProperty.calculateTenantTypePercentages(PostType.ROOM_RENTAL, TenantType.FEMALE));
			percentages.put(TenantType.COUPLE,
					svcProperty.calculateTenantTypePercentages(PostType.ROOMMATE_FINDING, TenantType.COUPLE));
			percentages.put(TenantType.FAMILY,
					svcProperty.calculateTenantTypePercentages(PostType.ROOMMATE_FINDING, TenantType.FAMILY));
			return new ResponseEntity<>(percentages, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* View Count */
	@PatchMapping("/{id}/views")
	public ResponseEntity<?> patchIncreaseViewCount(@PathVariable Long id) {
		try {
			Long currentViewCount = svcProperty.increaseViewCount(id);

			Map<String, String> resBody = new HashMap<String, String>();
			resBody.put("id", String.valueOf(id));
			resBody.put("viewCount", String.valueOf(currentViewCount));

			return new ResponseEntity<>(resBody, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/export/{reportType}")
	public void exportListOfPropertiesByRoomRentalPostType(HttpServletResponse response,
			@PathVariable String reportType) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=report_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		List<Property> list = new ArrayList<>();
		if (reportType.equals("properties-by-room-rental")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-roommate")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-male-roommate")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING
							&& p.getTenantType() == TenantType.MALE)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-female-roommate")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING
							&& p.getTenantType() == TenantType.FEMALE)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-male-only-room-rental")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getTenantType() == TenantType.MALE)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-female-only-room-rental")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getTenantType() == TenantType.FEMALE)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-couple-only-room-rental")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getTenantType() == TenantType.COUPLE)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-room-rental-for-hdb")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getPropertyType() == PropertyType.HDB)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-room-rental-for-condominium")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getPropertyType() == PropertyType.CONDOMINIUM)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-room-rental-for-landed")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getPropertyType() == PropertyType.LANDED)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-roommate-finding-for-hdb")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING
							&& p.getPropertyType() == PropertyType.HDB)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-roommate-finding-for-condominium")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING
							&& p.getPropertyType() == PropertyType.CONDOMINIUM)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-roommate-finding-for-landed")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING
							&& p.getPropertyType() == PropertyType.LANDED)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-room-rental-for-singles")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getRoomType() == RoomType.SINGLE)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-room-rental-for-common")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getRoomType() == RoomType.COMMON)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-room-rental-for-master")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getRoomType() == RoomType.MASTER)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-room-rental-for-whole")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOM_RENTAL
							&& p.getRoomType() == RoomType.WHOLE_UNIT)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-roommate-finding-for-singles")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING
							&& p.getRoomType() == RoomType.SINGLE)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-roommate-finding-for-common")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING
							&& p.getRoomType() == RoomType.COMMON)
					.collect(Collectors.toList());
		} else if (reportType.equals("properties-by-roommate-finding-for-master")) {
			list = svcProperty.getAllReportProperties().stream()
					.filter(p -> p.getPostType() == PostType.ROOMMATE_FINDING
							&& p.getRoomType() == RoomType.MASTER)
					.collect(Collectors.toList());
		}

		String[] csvHeader = { "Id", "Title", "Description", "Rental Fees", "Address", "Postal Code",
				"Room Area", "Num of Bedrooms", "Num of Bathrooms", "Num of Tenants",
				"Property Type", "Room Type", "Post Type", "Approve Status", "Status" };
		String[] nameMapping = { "id", "title", "description", "rentalFees", "address", "postalCode",
				"roomArea", "numBedrooms", "numBathrooms", "numTenants",
				"propertyType", "roomType", "postType", "approveStatus", "status" };

		csvWriter.writeHeader(csvHeader);

		for (Property property : list) {
			csvWriter.write(property, nameMapping);
		}

		csvWriter.close();
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<?> approveProperty(@PathVariable Long id) {
		try {
			Boolean approved = svcProperty.approveProperty(id);
			if (approved) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}/decline")
	public ResponseEntity<?> declineProperty(@PathVariable Long id) {
		try {
			Boolean declined = svcProperty.declineProperty(id);
			if (declined) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
