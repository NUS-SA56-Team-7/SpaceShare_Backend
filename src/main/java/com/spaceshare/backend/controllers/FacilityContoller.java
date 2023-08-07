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
        List<Facility> facilities = facilityService.getAllFacilities();
        if (facilities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(facilities);
    }

    @GetMapping("/getFacilityById/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable Long id) {
        Facility facility = facilityService.getFacilityById(id);
        if (facility == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facility);
    }

    /**
     * create
     * 
     * @param facility
     * @return
     */
    @PostMapping
    public ResponseEntity<Facility> addFacility(@RequestBody @Validated Facility facility) {
        Facility addedFacility = facilityService.addFacility(facility);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFacility);
    }

    /**
     * update
     * 
     * @param facility
     * @return
     */
    @PutMapping
    public ResponseEntity<Facility> updateFacility(@RequestBody @Validated Facility facility) {
        Facility updatedFacility = facilityService.updateFacility(facility);
        if (updatedFacility == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFacility);
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
        boolean deleted = facilityService.deleteFacility(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
