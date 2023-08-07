package com.spaceshare.backend.controllers;

import org.springframework.validation.annotation.Validated;

import com.spaceshare.backend.models.Amenity;
import com.spaceshare.backend.services.AmenityService;

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

import java.util.List;

@RestController
@RequestMapping("/api/amenity")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    /**
     * find all
     * 
     * @return
     */
    @GetMapping("/getAllAmenities")
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();
        if (amenities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(amenities);
    }

    /**
     * new find
     * 
     * @param id
     * @return
     */
    @GetMapping("/getAmenityById/{id}")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable Long id) {
        Amenity amenity = amenityService.getAmenityById(id);
        if (amenity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(amenity);
    }

    /**
     * create
     * 
     * @param amenity
     * @return
     */
    @PostMapping
    public ResponseEntity<Amenity> addAmenity(@RequestBody @Validated Amenity amenity) {
        Amenity addedAmenity = amenityService.addAmenity(amenity);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAmenity);
    }

    /**
     * update name by id
     * 
     * @param amenity
     * @return
     */
    @PutMapping
    public ResponseEntity<Amenity> updateAmenity(@RequestBody @Validated Amenity amenity) {
        Amenity updatedAmenity = amenityService.updateAmenity(amenity);
        if (updatedAmenity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAmenity);
    }

    /**
     * delete
     * 
     * @param id
     * @param amenityName
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        boolean deleted = amenityService.deleteAmenity(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
