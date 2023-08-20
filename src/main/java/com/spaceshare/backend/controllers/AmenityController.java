package com.spaceshare.backend.controllers;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.DuplicateResourceException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Amenity;
import com.spaceshare.backend.services.AmenityService;

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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/amenity")
public class AmenityController {

    /*** Services ***/
    @Autowired
    AmenityService amenityService;

    /*** API Methods ***/
    @GetMapping("")
    public ResponseEntity<?> getAllAmenities() {
        try {
            List<Amenity> amenities = amenityService.getAllAmenities();
            if (amenities.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(amenities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAmenityById(@PathVariable Long id) {
        try {
            Amenity amenity = amenityService.getAmenityById(id);
            return new ResponseEntity<>(amenity, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> postNewAmenity(
            @RequestBody Amenity amenity) {
        try {
            amenityService.addAmenity(amenity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DuplicateResourceException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAmenity(@PathVariable Long id, @RequestBody Amenity amenity) {
        try {
            amenityService.updateAmenity(id, amenity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DuplicateResourceException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAmenity(@PathVariable Long id) {
        try {
            Boolean deleted = amenityService.deleteAmenity(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/export/list")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=amenities_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		List<Amenity> list = amenityService.getAllAmenities().stream()
            .sorted(Comparator.comparingLong(Amenity::getId))
            .collect(Collectors.toList());

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Amenity Id", "Amenity Name" };
		String[] nameMapping = { "id", "amenityName" };

		csvWriter.writeHeader(csvHeader);

		for (Amenity amenity : list) {
			csvWriter.write(amenity, nameMapping);
		}
		csvWriter.close();
	}

}
