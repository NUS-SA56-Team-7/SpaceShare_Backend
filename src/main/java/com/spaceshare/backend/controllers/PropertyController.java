package com.spaceshare.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.dtos.CommentDTO;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Comment;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.models.enums.PostType;
import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.TenantType;
import com.spaceshare.backend.models.enums.RoomType;
import com.spaceshare.backend.projections.PropertyDetailProjection;
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
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getAllProperties() {
		try {
			return new ResponseEntity<>(svcProperty.getAllProperties().subList(0, 10), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Comments */
	@GetMapping("/{id}/comments")
	public ResponseEntity<?> getAllComments(@PathVariable Long id) {
		try {
			List<CommentDTO> comments = svcComment.getBaseComments(id);
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
}
