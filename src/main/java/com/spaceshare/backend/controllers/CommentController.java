package com.spaceshare.backend.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.services.CommentService;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

	/*** Services ***/
	@Autowired
	CommentService svcComment;
	
	/*** API Methods ***/
	@PostMapping("/create")
	public ResponseEntity<?> postCreateComment(
			@RequestBody Map<String, String> reqBody) {
		try {
			Boolean success = svcComment.createComment(
					UUID.fromString((String)reqBody.get("tenantId")),
					Long.parseLong(reqBody.get("propertyId")),
					reqBody.get("comment"));
			
			if (success) {
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
	
	@PostMapping("/reply/create")
	public ResponseEntity<?> postCreateReply(
			@RequestBody Map<String, String> reqBody) {
		try {
			Boolean success = svcComment.createReply(
					UUID.fromString((String)reqBody.get("tenantId")),
					Long.parseLong(reqBody.get("commentId")),
					reqBody.get("reply"));
			
			if (success) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCommentAndReplies(@PathVariable Long id) {
		try {
			Boolean success = svcComment.deleteCommentAndReplies(id);
			
			if (success) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/reply/delete/{id}")
	public ResponseEntity<?> deleteReply(@PathVariable Long id) {
		try {
			Boolean success = svcComment.deleteReply(id);
			
			if (success) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
