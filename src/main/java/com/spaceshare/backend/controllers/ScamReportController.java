package com.spaceshare.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.ScamReport;
import com.spaceshare.backend.services.ScamReportService;
@RestController
@RequestMapping("/api/scamreport")
public class ScamReportController {

	/*** Services ***/
    @Autowired
    ScamReportService scamReportService;

	/*** API Methods ***/
    @GetMapping("")
	public ResponseEntity<List<ScamReport>> getAllScamReports() {

		try {
			List<ScamReport> scamReports = scamReportService.getAllScamReports();
			if (scamReports == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<ScamReport>>(scamReports, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/status")
	public ResponseEntity<ScamReport> getScamReportsStatus() {

		try {
			
			ScamReport scamReport = scamReportService.getScamReportsStatus();
			if (scamReport == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<ScamReport>(scamReport, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

    @PostMapping("/create/{propertyId}")
	public ResponseEntity<ScamReport> createScamReport(@PathVariable Long propertyId) {
		try {
			Boolean success = scamReportService.createScamReport(propertyId);

			if (success) {
				return new ResponseEntity<ScamReport>(HttpStatus.CREATED);
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

    @PutMapping("/approved/{id}")
	public ResponseEntity<ScamReport> approveScamReport(@PathVariable("id") Long id) {

		try {

            Boolean success = scamReportService.approveScamReport(id);

			if (success)
				return new ResponseEntity<>(HttpStatus.OK);
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

    @PutMapping("/declined/{id}")
	public ResponseEntity<ScamReport> declineScamReport(@PathVariable("id") Long id) {

		try {

            Boolean success = scamReportService.declineScamReport(id);

			if (success)
				return new ResponseEntity<>(HttpStatus.OK);
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
}
