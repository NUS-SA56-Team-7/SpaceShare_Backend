package com.spaceshare.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Comment;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.models.enums.PostType;
import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.RoomMateType;
import com.spaceshare.backend.models.enums.RoomType;
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
	@PostMapping("/create")
	public ResponseEntity<?> postCreateProperty(
			@RequestBody Property property) {

		Boolean success = svcProperty.createProperty(property);
		if (success) {
			return new ResponseEntity<>(property, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPropertyById(@PathVariable Long id) {
		try {
			Property property = svcProperty.getPropertyById(id);
			return new ResponseEntity<>(property, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Comments */
	@GetMapping("/{id}/comments")
	public ResponseEntity<?> getAllComments(@PathVariable Long id) {
		try {
			List<Comment> comments = svcComment.getBaseComments(id);
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
	 * average Room Rentals by roommate type
	 */
	@GetMapping("/room-rental/roommate-type/percentages")
	public ResponseEntity<Map<RoomMateType, Double>> getRoomRentalPercentagesByRoomMateType() {
		Map<RoomMateType, Double> percentages = new HashMap<>();
		try {
			percentages.put(RoomMateType.MALE,
					svcProperty.calculateRoomMateTypePercentages(PostType.ROOM_RENTAL, RoomMateType.MALE));
			percentages.put(RoomMateType.FEMALE,
					svcProperty.calculateRoomMateTypePercentages(PostType.ROOM_RENTAL, RoomMateType.FEMALE));
			percentages.put(RoomMateType.COUPLE,
					svcProperty.calculateRoomMateTypePercentages(PostType.ROOM_RENTAL, RoomMateType.COUPLE));
			return new ResponseEntity<>(percentages, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * average Roommate Findings by roommate type
	 */
	@GetMapping("/roommate-finding/roommate-type/percentages")
	public ResponseEntity<Map<RoomMateType, Double>> getRoommateFindingPercentagesByRoomMateType() {
		Map<RoomMateType, Double> percentages = new HashMap<>();
		try {
			percentages.put(RoomMateType.MALE,
					svcProperty.calculateRoomMateTypePercentages(PostType.ROOM_RENTAL, RoomMateType.MALE));
			percentages.put(RoomMateType.FEMALE,
					svcProperty.calculateRoomMateTypePercentages(PostType.ROOM_RENTAL, RoomMateType.FEMALE));
			percentages.put(RoomMateType.COUPLE,
					svcProperty.calculateRoomMateTypePercentages(PostType.ROOMMATE_FINDING, RoomMateType.COUPLE));
			return new ResponseEntity<>(percentages, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
