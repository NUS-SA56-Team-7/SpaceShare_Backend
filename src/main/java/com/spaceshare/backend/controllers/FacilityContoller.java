package com.spaceshare.backend.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Facility;
import com.spaceshare.backend.services.FacilityService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/facility")
public class FacilityContoller {

    private final FacilityService facilityService;

    public FacilityContoller(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping("/getAllFacilities")
    public ResponseEntity<List<Facility>> getAllFacilities() {
        try {
            List<Facility> facilities = facilityService.getAllFacilities();
            if (facilities.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return new ResponseEntity<>(facilities, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getFacilityById/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable Long id) {
        try {
            Facility facility = facilityService.getFacilityById(id);
            if (facility == null) {
                return ResponseEntity.notFound().build();
            }
            // return ResponseEntity.ok(facility);
            return new ResponseEntity<>(facility, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * create
     * 
     * @param facility
     * @return
     */
    @PostMapping
    public ResponseEntity<Facility> addFacility(@RequestBody @Validated Facility facility) {
        try {
            // Facility addedFacility = facilityService.addFacility(facility);
            boolean success = facilityService.addFacility(facility);

            if (success) {
                // return ResponseEntity.status(HttpStatus.CREATED).body(addedFacility);
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

    /**
     * update
     * 
     * @param facility
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Facility> updateFacility(@PathVariable Long id, @RequestBody @Validated Facility facility) {
        try {
            // Facility updatedFacility = facilityService.updateFacility(id, facility);
            boolean success = facilityService.updateFacility(id, facility);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            // return ResponseEntity.ok(updatedFacility);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * delete
     * 
     * @param id
     * @param facilityName
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        try {
            boolean deleted = facilityService.deleteFacility(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
