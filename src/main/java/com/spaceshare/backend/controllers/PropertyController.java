package com.spaceshare.backend.controllers;

import java.util.List;

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
		}
		else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getPropertyById(@PathVariable Long id) {
		try {
			Property property = svcProperty.getPropertyById(id);
			return new ResponseEntity<>(property, HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Comments */
	@GetMapping("/{id}/comments")
	public ResponseEntity<?> getAllComments(@PathVariable Long id) {
		try {
			List<Comment> comments = svcComment.getBaseComments(id);
			return new ResponseEntity<>(comments, HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
