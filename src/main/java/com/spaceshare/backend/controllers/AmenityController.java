package com.spaceshare.backend.controllers;

import org.springframework.validation.annotation.Validated;

import com.spaceshare.backend.exceptions.BadRequestException;
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

import java.util.List;

@RestController
@RequestMapping("/api/amenity")
public class AmenityController {

    /*** Services ***/
    @Autowired
    AmenityService amenityService;

    /*** API Methods ***/
    /**
     * find all
     * 
     * @return
     */
    @GetMapping("/getAllAmenities")
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        try {
            // List<Amenity> amenities = amenityService.getAllAmenities();
            List<Amenity> amenities = amenityService.getAllAmenities();
            if (amenities.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return new ResponseEntity<>(amenities, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * new find
     * 
     * @pathvariable id
     * @return
     */
    @GetMapping("/getAmenityById/{id}")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable Long id) {
        try {
            Amenity amenity = amenityService.getAmenityById(id);
            if (amenity == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(amenity, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * create
     * 
     * @requestbody amenity
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<Amenity> addAmenity(@RequestBody @Validated Amenity amenity) {
        try {
            Boolean success = amenityService.addAmenity(amenity);

            if (success) {
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
     * update name by id
     * 
     * @pathvariable id
     * @requestbody amenity
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable Long id, @RequestBody @Validated Amenity amenity) {
        try {
            Boolean success = amenityService.updateAmenity(id, amenity);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
     * @pathvariable id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        try {
            Boolean deleted = amenityService.deleteAmenity(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return ResponseEntity.notFound().build();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
